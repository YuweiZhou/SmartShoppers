<?php 
require_once 'DB_Functions.php';
$db = new DB_Functions();
		
		
$insData = array(
    'name' => 'apple',
	'store_id' => '1',
	'price' => '.99',
	'type' => '1'
);

$data = $db->dataSynth($insData);

echo "hello world";
?>