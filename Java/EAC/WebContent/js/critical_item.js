// The root URL for the RESTful services
var rootURL = "http://localhost:8080/eac/rest/critical_item";

var currentCriticalItem;

// Retrieve critical_item list when application starts
findAll();

// Nothing to delete in initial application state
$('#btnDelete').hide();

// Register listeners
$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

// Trigger search when pressing 'Return' on search key input field
$('#searchKey').keypress(function(e) {
	if (e.which == 13) {
		search($('#searchKey').val());
		e.preventDefault();
		return false;
	}
});

// Add CriticalItem
$('#btnAdd').click(function() {
	newCriticalItem();
	return false;
});

// Save CriticalItem
$('#btnSave').click(function() {
	if ($('#critical_itemId').val() == '')
		addCriticalItem();
	else
		updateCriticalItem();
	return false;
});

// Delete CriticalItem
$('#btnDelete').click(function() {
	deleteCriticalItem();
	return false;
});

// Search by Id
$('#critical_itemList a').live('click', function() {
	findById($(this).data('identity'));
});

// Replace broken images with generic critical_item bottle
$("img").error(function() {
	$(this).attr("src", "pics/generic.jpg");

});

// Functions

// Search
function search(searchKey) {
	if (searchKey == '')
		findAll();
	else
		findByName(searchKey);
}

// New CriticalItem
function newCriticalItem() {
	$('#btnDelete').hide();
	currentCriticalItem = {};
	renderDetails(currentCriticalItem); // Display empty form
}

// Find all CriticalItems
function findAll() {
	console.log('findAll');
	$.ajax({
		type : 'GET',
		url : rootURL,
		dataType : "json", // data type of response
		success : renderList
	});
}

// Find by name
function findByName(searchKey) {
	console.log('findByName: ' + searchKey);
	$.ajax({
		type : 'GET',
		url : rootURL + '/search/' + searchKey,
		dataType : "json",
		success : renderList
	});
}

// Find by Id
function findById(id) {
	console.log('findById: ' + id);
	$.ajax({
		type : 'GET',
		url : rootURL + '/' + id,
		dataType : "json",
		success : function(data) {
			$('#btnDelete').show();
			console.log('findById success: ' + data.name);
			currentCriticalItem = data;
			renderDetails(currentCriticalItem);
		}
	});
}

// Add CriticalItem
function addCriticalItem() {
	console.log('addCriticalItem');
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : rootURL,
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('CriticalItem created successfully');
			$('#btnDelete').show();
			$('#critical_itemId').val(data.id);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('addCriticalItem error: ' + textStatus);
		}
	});
}

// Update CriticalItem
function updateCriticalItem() {
	console.log('updateCriticalItem');
	$.ajax({
		type : 'PUT',
		contentType : 'application/json',
		url : rootURL + '/' + $('#critical_itemId').val(),
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('CriticalItem updated successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('updateCriticalItem error: ' + textStatus);
		}
	});
}

// Delete CriticalItem
function deleteCriticalItem() {
	console.log('deleteCriticalItem');
	$.ajax({
		type : 'DELETE',
		url : rootURL + '/' + $('#critical_itemId').val(),
		success : function(data, textStatus, jqXHR) {
			alert('CriticalItem deleted successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('deleteCriticalItem error');
		}
	});
}

// Utility functions
// ....

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an
	// object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$('#critical_itemList li').remove();
	$.each(list, function(index, critical_item) {
		$('#critical_itemList').append(
				'<li><a href="#" data-identity="' + critical_item.id + '">'
						+ critical_item.name + '</a></li>');
	});
}

function renderDetails(critical_item) {
	$('#critical_itemId').val(critical_item.id);
	$('#name').val(critical_item.name);
	$('#email').val(critical_item.email);
	$('#expertise').val(critical_item.expertise);
	$('#description').val(critical_item.description);
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	var critical_itemId = $('#critical_itemId').val();
	return JSON.stringify({
		"id" : critical_itemId == "" ? null : critical_itemId,
		"name" : $('#name').val(),
		"email" : $('#email').val(),
		"expertise" : $('#expertise').val(),
		"description" : $('#description').val()
	});
}
