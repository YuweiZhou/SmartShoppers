<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {
	
    // Get tag
    $tag = $_POST['tag'];
 
    // Include Database handler
    require_once 'DB_Functions.php';
    $db = new DB_Functions();
	
	//Login --------------------------------------------------------------------------------------------
	
	if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
			$response["user"]["user_id"] = $user["user_id"];
            $response["user"]["email"] = $user["email"];
			$response["user"]["user_type"] = $user["user_type"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    }
	
	//Login --------------------------------------------------------------------------------------------
	
	
	//Change Password ----------------------------------------------------------------------------------
	
	 else if ($tag == 'chgpass'){
		$email = $_POST['email'];
 
		$newpassword = $_POST['new_password'];
 
		$hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
		$subject = "Change Password Notification";
        $message = "Hello User,nnYour Password is sucessfully changed for SmartShoppers.";
        $from = "SmartShoppers";
        $headers = "From:" . $from;
		if ($db->isUserExisted($email)) {
 
			$user = $db->forgotPassword($email, $encrypted_password, $salt);
			if ($user) {
				$response["success"] = 1;
				mail($email,$subject,$message,$headers);
				echo json_encode($response);
			}
			else {
				$response["error"] = 1;
				echo json_encode($response);
			}
 
            // user is already existed - error response
 
        }
        else {
            $response["error"] = 2;
            $response["error_msg"] = "User not exist";
            echo json_encode($response);
		}
}
	
	//Change Password -----------------------------------------------------------------------------------
	
	
	//Forgot Password ----------------------------------------------------------------------------------
	
	else if ($tag == 'forpass'){
		$forgotpassword = $_POST['email'];
 
		$randomcode = $db->random_string();
 
		$hash = $db->hashSSHA($randomcode);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
		$subject = "Password Recovery";
        $message = "Hello User,nnYour Password is sucessfully changed. Your new Password is $randomcode . Login with your new Password and change it in user settings.";
        $from = "SmartShoppers";
        $headers = "From:" . $from;
		
        if ($db->isUserExisted($forgotpassword)) {
 
			$user = $db->forgotPassword($forgotpassword, $encrypted_password, $salt);
			if ($user) {
				$response["success"] = 1;
					mail($forgotpassword,$subject,$message,$headers);
					echo json_encode($response);
			}
			else {
				$response["error"] = 1;
				echo json_encode($response);
			}
 
        }
        else {
 
            $response["error"] = 2;
            $response["error_msg"] = "User not exist";
            echo json_encode($response);
 
		}
 
	}
	
	//Forgot Password -----------------------------------------------------------------------------------
	
	
	//Registry --------------------------------------------------------------------------------------------
	
    if ($tag == 'register') {
        // Request type is Register new user
        $email = $_POST['email'];
        $password = $_POST['password'];
		$user_type = $_POST['user_type'];
		$first_name = $_POST['first_name'];
		$last_name = $_POST['last_name'];
		$address = $_POST['address'];
		$city = $_POST['city'];
		$state = $_POST['state'];
		$zip = $_POST['zip'];
		$card_num = $_POST['card_num'];
 
        $subject = "Registration";
        $message = "Hello $fname,nnYou have sucessfully registered to SmartShoppers.";
        $from = "SmartShoppers";
        $headers = "From:" . $from;
 
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        }
           else if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);
		}
		else {
            // store user
            $user = $db->storeUser( $email, $password, $user_type, $first_name, $last_name, $address, $city, $state, $zip, $card_num);
            if ($user) {
                // user stored successfully
				$response["success"] = 1;
				$response["user"]["email"] = $user["email"];
				$response["user"]["password"] = $user["password"];
				$response["user"]["user_type"] = $user["user_type"];
				$response["user"]["first_name"] = $user["first_name"];
				$response["user"]["last_name"] = $user["last_name"];
				$response["user"]["address"] = $user["address"];
				$response["user"]["city"] = $user["city"];
				$response["user"]["state"] = $user["state"];
				$response["user"]["zip"] = $user["zip"];
				$response["user"]["encrypted_card"] = $user["encrypted_card"];
				
                mail($email,$subject,$message,$headers);
 
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } 
	
	//Registry --------------------------------------------------------------------------------------------
	
	
	

}

	
	
?>