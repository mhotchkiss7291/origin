// The root URL for the RESTful services
var rootURL = "http://localhost:8080/eac/rest/critic";

var currentCritic;

// Retrieve critic list when application starts
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

// Add Critic
$('#btnAdd').click(function() {
	newCritic();
	return false;
});

// Save Critic
$('#btnSave').click(function() {
	if ($('#criticId').val() == '')
		addCritic();
	else
		updateCritic();
	return false;
});

// Delete Critic
$('#btnDelete').click(function() {
	deleteCritic();
	return false;
});

// Search by Id
$('#criticList a').live('click', function() {
	findById($(this).data('identity'));
});

// Replace broken images with generic critic bottle
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

// New Critic
function newCritic() {
	$('#btnDelete').hide();
	currentCritic = {};
	renderDetails(currentCritic); // Display empty form
}

// Find all Critics
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
			currentCritic = data;
			renderDetails(currentCritic);
		}
	});
}

// Add Critic
function addCritic() {
	console.log('addCritic');
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : rootURL,
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Critic created successfully');
			$('#btnDelete').show();
			$('#criticId').val(data.id);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('addCritic error: ' + textStatus);
		}
	});
}

// Update Critic
function updateCritic() {
	console.log('updateCritic');
	$.ajax({
		type : 'PUT',
		contentType : 'application/json',
		url : rootURL + '/' + $('#criticId').val(),
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Critic updated successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('updateCritic error: ' + textStatus);
		}
	});
}

// Delete Critic
function deleteCritic() {
	console.log('deleteCritic');
	$.ajax({
		type : 'DELETE',
		url : rootURL + '/' + $('#criticId').val(),
		success : function(data, textStatus, jqXHR) {
			alert('Critic deleted successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('deleteCritic error');
		}
	});
}

// Utility functions
// ....

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an
	// object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$('#criticList li').remove();
	$.each(list, function(index, critic) {
		$('#criticList').append(
				'<li><a href="#" data-identity="' + critic.id + '">'
						+ critic.name + '</a></li>');
	});
}

function renderDetails(critic) {
	$('#criticId').val(critic.id);
	$('#name').val(critic.name);
	$('#email').val(critic.email);
	$('#expertise').val(critic.expertise);
	$('#description').val(critic.description);
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	var criticId = $('#criticId').val();
	return JSON.stringify({
		"id" : criticId == "" ? null : criticId,
		"name" : $('#name').val(),
		"email" : $('#email').val(),
		"expertise" : $('#expertise').val(),
		"description" : $('#description').val()
	});
}
