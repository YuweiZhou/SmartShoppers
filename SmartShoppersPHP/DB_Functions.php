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
	
	
 
}
 
?>