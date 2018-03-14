<?php	

	//Page Statistics
	
	//Most Recent Sale/////////////////////////////////////////////////////////////////
		 
if (isset($_POST['tag']) && $_POST['tag'] != '') {	

		$tag = $_POST['tag'];	
		
		require_once 'DB_Functions.php';
		$db = new DB_Functions();
		
		if ($tag == 'map_stores') {
			$lat = $_POST['lat'];
			$lng = $_POST['lng'];
			$stores = $db->obtainGroceryStores($lat, $lng);

			if ($stores != false) {
				$return_arr = array();
				foreach($stores as $store){
					$response["store_id"] = $store["store_id"];
					$response["name"] = $store["name"];
					$response["address"] = $store["address"];
					$response["address"] = $store["address"];
					$response["city"] = $store["city"];
					$response["state"] = $store["state"];
					$response["lat"] = $store["lat"];
					$response["lng"] = $store["lng"];
					array_push($return_arr,$response);
				}
				echo json_encode($return_arr);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "No Stores in location!";
				echo json_encode($response);
			}
		}
	
}	  
		
		
			
		
?>