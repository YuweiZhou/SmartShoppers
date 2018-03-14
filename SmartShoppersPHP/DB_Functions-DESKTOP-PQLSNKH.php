<?php
 
class DB_Functions {
 
    private $con;
 
    //put your code here
    // constructor
    function __construct() {
		require_once 'common.php';
		try{
			$options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'); 
			$this->_db = new PDO('mysql:host='.$host.';dbname='.$database, $user, $password, array(PDO::ATTR_PERSISTENT => true));
			$this->_db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		} 
		catch(PDOException $ex) { 
			die("Failed to connect to the database: "); 
		} 
    }
 
    // destructor
    function __destruct() {
 
    }
	
	/**
     * Adding new user to mysql database
     * returns user details
     */
    public function storeUser($email, $password, $user_type, $first_name, $last_name, $address, $city, $state, $zip, $card_num) {
		
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
		
		$hash = $this->hashSSHA($card_num);
        $encrypted_card = $hash["encrypted"]; // encrypted password
        $card_salt = $hash["salt"]; // salt
		
		$query = " 
            INSERT INTO users ( 
                email, 
				password,
				salt,
				user_type,
				first_name,
				last_name,
				address,
				city,
				state,
				zip,
				encrypted_card,
				card_salt
            ) VALUES ( 
                :email, 
				:password,
				:salt,
				:user_type,
				:first_name,
				:last_name,
				:address,
				:city,
				:state,
				:zip,
				:encrypted_card,
				:card_salt
            ) 
        "; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':email',$email);
			$stmt->bindValue(':password',$encrypted_password);
			$stmt->bindValue(':salt',$salt);
			$stmt->bindValue(':user_type',$user_type);
			$stmt->bindValue(':first_name',$first_name);
			$stmt->bindValue(':last_name',$last_name);
			$stmt->bindValue(':address',$address);
			$stmt->bindValue(':city',$city);
			$stmt->bindValue(':state',$state);
			$stmt->bindValue(':zip',$zip);
			$stmt->bindValue(':encrypted_card',$encrypted_card);
			$stmt->bindValue(':card_salt',$card_salt);
            $stmt->execute(); 
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		$query = " 
            SELECT * from users WHERE email = :email;
        "; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':email',$email);
            $stmt->execute(); 
			$result = $stmt->fetch();
			return $result;
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }
		
    }
	
	/**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        		
		$query = " 
            SELECT * from users WHERE email = :email;
        "; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':email',$email);
            $stmt->execute(); 
			$result = $stmt->fetch();
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }
		
		if (!(empty($result))) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
			
    }
	
	
	 /**
     * Verifies user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
		
		$query = " 
            SELECT * from users WHERE email = :email;
        "; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':email',$email);
            $stmt->execute(); 
			$result = $stmt->fetch();
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }
		
		if(!(empty($result))){
			
			$salt = $result['salt'];
			$encrypted_password = $result['password'];
		    $hash = $this->checkhashSSHA($salt, $password);
			
		    if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
		
    }
	
	/**
     * Encrypting password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
	
	public function random_string()
	{
		$character_set_array = array();
		$character_set_array[] = array('count' => 7, 'characters' => 'abcdefghijklmnopqrstuvwxyz');
		$character_set_array[] = array('count' => 1, 'characters' => '0123456789');
		$temp_array = array();
		foreach ($character_set_array as $character_set) {
			for ($i = 0; $i < $character_set['count']; $i++) {
				$temp_array[] = $character_set['characters'][rand(0, strlen($character_set['characters']) - 1)];
			}
		}
		shuffle($temp_array);
		return implode('', $temp_array);
	}
 
	public function forgotPassword($forgotpassword, $newpassword, $salt){
		
		$query = " 
            UPDATE `users` SET `password` = :newpassword,`salt` = :salt WHERE `email` = :forgotpassword
        "; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':forgotpassword',$forgotpassword);
			$stmt->bindValue(':salt',$salt);
			$stmt->bindValue(':newpassword',$newpassword);
            $stmt->execute(); 
			return true;
        } 
        catch(PDOException $ex) 
        { 
			return false;
        }

	}
	
	public function obtainGroceryStores($lat,$lng) {
        		
		$query = "
		SELECT store_id, name, address, city, state, zip, lat, lng, ( 3959 * acos( cos( radians(:centerlat) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:centerlng) ) + sin( radians(:centerlat) ) * sin( radians( lat ) ) ) ) AS distance FROM grocery_stores HAVING distance < 100 ORDER BY distance;
		"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			$stmt->bindValue(':centerlat', $lat);
			$stmt->bindValue(':centerlng', $lng);
            $stmt->execute(); 
			$result = $stmt->fetchAll();
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }
		
		if (!(empty($result))) {
            return $result;
        } else {
            return false;
        }
			
    }
	
	/**
	*Check helpers_available table
	**/
	
	public function checkHelpersAvailable($user_id, $store_id){
		
			$query = " 
				SELECT * FROM helpers_available
				WHERE store_id = :store_id and user_id = :user_id
			"; 
			
			$stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':user_id',$user_id);
			$stmt->bindValue(':store_id',$store_id);
			
			$stmt->execute(); 
			$result = $stmt->fetchAll();
			if($stmt->rowCount() > 0){
				return 'Exists';
			}
			else {
				return;
			}
	}
	
	/**
     * Update helpers_available table
     * returns helpers if needed
     */
    public function updateAvailableHelpers($user_id, $store_id, $tag) {
		
		if($tag == 'users_available_add'){
		$combosAlreadyMade = $this->checkHelpersAvailable($user_id, $store_id);
		
		if($combosAlreadyMade == 'Exists'){
			return 'Exists';
		}
		}
		if ($tag == 'users_available_add') {
			$query = " 
				INSERT INTO helpers_available ( 
					store_id, 
					user_id
				) VALUES ( 
					:store_id, 
					:user_id
				) 
				"; 
		} else if ($tag == 'users_available_remove') {
			$query = " 
			DELETE FROM helpers_available
			WHERE store_id = :store_id and user_id = :user_id
				"; 
		}
		
		else if ($tag == 'users_remove_all') {
			$query = " 
			DELETE FROM helpers_available
			WHERE user_id = :user_id
				"; 
		}
		else if ($tag == 'users_select') {
			$query = " 
			select * FROM helpers_available
			WHERE store_id = :store_id
				"; 
		}
		
		
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			if ($tag != 'users_select') {
				$stmt->bindValue(':user_id',$user_id);
			}
			if ($tag != 'users_remove_all') {
				$stmt->bindValue(':store_id',$store_id);
			}
            $stmt->execute(); 
			if ($tag == 'users_select') {
				$result = $stmt->fetchAll();
				return $result;
			}
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	
	/**
	*Check help_requests table for existing request
	**/
	
	public function checkHelpRequests($helper_id, $request_help_id ){
		
			$query = " 
				SELECT * FROM help_requests
				WHERE helper_id = :helper_id and request_help_id = :request_help_id
			"; 
			
			$stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':helper_id',$helper_id);
			$stmt->bindValue(':request_help_id',$request_help_id);
			
			$stmt->execute(); 
			$result = $stmt->fetchAll();
			if($stmt->rowCount() > 0){
				return 'Exists';
			}
			else {
				return;
			}
	}
	
	
	/**
     * Update helpers_available table
     * returns helpers if needed
     */
    public function updateHelpRequests($helper_id, $request_help_id, $tag) {
		
		if($tag == 'help_request_add'){
		$combosAlreadyMade = $this->checkHelpRequests($helper_id, $request_help_id);
		
		if($combosAlreadyMade == 'Exists'){
			return 'Exists';
		}
		}
		
		if ($tag == 'help_request_add') {
			$query = " 
				INSERT INTO help_requests ( 
					helper_id, 
					request_help_id
				) VALUES ( 
					:helper_id, 
					:request_help_id
				) 
				"; 
		} else if ($tag == 'help_request_remove') {
			$query = " 
			DELETE FROM help_requests
			WHERE helper_id = :helper_id and request_help_id = :request_help_id
				"; 
		}
		
		else if ($tag == 'requests_remove_all') {
			$query = " 
			DELETE FROM help_requests
			WHERE helper_id = :helper_id
				"; 
		}
		else if ($tag == 'requests_select') {
			$query = " 
			select * FROM help_requests
			WHERE helper_id = :helper_id
				"; 
		}
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':helper_id',$helper_id);
			
			if ($tag != 'requests_remove_all' and $tag != 'requests_select') {
				$stmt->bindValue(':request_help_id',$request_help_id);
			}
            $stmt->execute(); 
			if ($tag == 'requests_select') {
				$result = $stmt->fetchAll();
				return $result;
			}
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	/**
     * Return Massive Array of all groceries
     * 
     */
    public function getGroceries($store_id) {
		
		
		
			$query = " 
			select * FROM groceries
			WHERE store_id = :store_id
				"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':store_id',$store_id);
			
            $stmt->execute(); 
		
			$result = $stmt->fetchAll();
			return $result;
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	/**
     * Return User Position array of groceries
     * 
     */
    public function getUserGroceries($user_id) {
		
		
		
				$query = " 
			select * FROM user_grocery_list
			WHERE user_id = :user_id
				"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':user_id',$user_id);
			
            $stmt->execute(); 
		
			$result = $stmt->fetch();
			return $result;
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	/**
     * Insert into user groceries page
     * 
     */
    public function insertUserGroceries($user_id, $position_array) {
		
		
		
			$query = " 
			INSERT INTO user_grocery_list ( 
					user_id, 
					position_array
				) VALUES ( 
					:user_id, 
					:position_array
				)
				"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':user_id',$user_id);
			$stmt->bindValue(':position_array',$position_array);
			
            $stmt->execute(); 
		
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	/**
     * Insert into user groceries page
     * 
     */
    public function deleteUserGroceries($user_id) {
		
			$query = " 
			DELETE FROM user_grocery_list
			WHERE user_id = :user_id
				"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':user_id',$user_id);
			
            $stmt->execute(); 
		
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
	/**
     * Return Massive Array of all groceries
     * 
     */
    public function selectUsersById($user_id) {
		
		
		
			$query = " 
			select * FROM users
			WHERE user_id = :user_id
				"; 
		
		try 
        { 
            $stmt = $this->_db->prepare($query); 
			
			$stmt->bindValue(':user_id',$user_id);
			
            $stmt->execute(); 
		
			$result = $stmt->fetchAll();
			return $result;
        } 
        catch(PDOException $ex) 
        { 
			$h = $ex->getMessage ();
            die($h); 
        }

		
		
    }
	
 
}
 
?>