<?php	

	//Page Statistics
	
	//Most Recent Sale/////////////////////////////////////////////////////////////////
		 
if (isset($_POST['tag']) && $_POST['tag'] != '') {	

		$tag = $_POST['tag'];	
		require_once 'DB_Functions.php';
		$db = new DB_Functions();
		if ($tag == 'help_request_add' or $tag == 'help_request_remove' or $tag == 'requests_remove_all' or $tag == 'requests_select') {
			$helper_id = $_POST['helper_id'];
			$request_help_id = $_POST['request_help_id'];
			$help_requests = $db->updateHelpRequests($helper_id, $request_help_id ,$tag);

			if($help_requests  != 'Exists'){
			if ($tag == 'requests_select') {
				$return_arr = array();
				foreach($help_requests as $request){
					$response["helper_id"] = $request["helper_id"];
					$response["request_help_id"] = $request["request_help_id"];
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