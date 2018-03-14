<?php	

	//Page Statistics
	
	//Most Recent Sale/////////////////////////////////////////////////////////////////
		 
if (isset($_POST['tag']) && $_POST['tag'] != '') {	

		$tag = $_POST['tag'];	
		require_once 'DB_Functions.php';
		$db = new DB_Functions();
		if ($tag == 'users_available_add' or $tag == 'users_available_remove' or $tag == 'users_remove_all' or $tag == 'users_select') {
			$user_id = $_POST['user_id'];
			$store_id = $_POST['store_id'];
			$users_available = $db->updateAvailableHelpers($user_id, $store_id, $tag);

			if($users_available != 'Exists'){
				if ($tag == 'users_select') {
					$return_arr = array();
					foreach($users_available as $user){
						$response["user_id"] = $user["user_id"];
						$response["store_id"] = $user["store_id"];
						array_push($return_arr,$response);
					}
					echo json_encode($return_arr);
				} else {
					$response["success"] = 1;
					echo json_encode($response);
				}
			 } else {
				$response["Failure"] = 3;
				echo json_encode($response);
			}
		}
	
}	  
		
		
			
		
?>