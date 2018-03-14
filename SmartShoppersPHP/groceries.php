<?php	

		 
if (isset($_POST['tag']) && $_POST['tag'] != '') {	

		$tag = $_POST['tag'];	
		
		require_once 'DB_Functions.php';
		$db = new DB_Functions();
		if ($tag == 'grocery_list') {
			$store_id = $_POST['store_id'];
			$groceries = $db->getGroceries($store_id);

			
				$return_arr = array();
				foreach($groceries as $grocery){
					$response["name"] = $grocery["name"];
					$response["price"] = $grocery["price"];
					$response["type"] = $grocery["type"];
					$response["unit"] = $grocery["unit"];
					array_push($return_arr,$response);
				}
				echo json_encode($return_arr);
			
		}
		if ($tag == 'user_grocery_list_select') {
			$user_id = $_POST['user_id'];
			$groceries = $db->getUserGroceries($user_id);
			
			$response["user_id"] = $groceries["user_id"];
			$response["position_array"] = $groceries["position_array"];
					
			echo json_encode($response);
			
		}
		
		if ($tag == 'user_grocery_list_add') {
			$user_id = $_POST['user_id'];
			$position_array = $_POST['position_array'];
			
			$groceries = $db->insertUserGroceries($user_id, $position_array);

			$response["success"] = 1;
					
			echo json_encode($response);
			
		}
		
		if ($tag == 'user_grocery_list_remove') {
			$user_id = $_POST['user_id'];
			
			$groceries = $db->removeUserGroceries($user_id);

			$response["success"] = 1;
					
			echo json_encode($response);
			
		}
	
}	  
		
		
			
		
?>