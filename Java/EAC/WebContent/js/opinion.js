// The root URL for the RESTful services
var rootURL = "http://localhost:8080/eac/rest/opinion";

var currentOpinion;

// Retrieve opinion list when application starts
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

// Add Opinion
$('#btnAdd').click(function() {
	newOpinion();
	return false;
});

// Save Opinion
$('#btnSave').click(function() {
	if ($('#opinionId').val() == '')
		addOpinion();
	else
		updateOpinion();
	return false;
});

// Delete Opinion
$('#btnDelete').click(function() {
	deleteOpinion();
	return false;
});

// Search by Id
$('#opinionList a').live('click', function() {
	findById($(this).data('identity'));
});

// Replace broken images with generic opinion bottle
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

// New Opinion
function newOpinion() {
	$('#btnDelete').hide();
	currentOpinion = {};
	renderDetails(currentOpinion); // Display empty form
}

// Find all Opinions
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
			currentOpinion = data;
			renderDetails(currentOpinion);
		}
	});
}

// Add Opinion
function addOpinion() {
	console.log('addOpinion');
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : rootURL,
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Opinion created successfully');
			$('#btnDelete').show();
			$('#opinionId').val(data.id);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('addOpinion error: ' + textStatus);
		}
	});
}

// Update Opinion
function updateOpinion() {
	console.log('updateOpinion');
	$.ajax({
		type : 'PUT',
		contentType : 'application/json',
		url : rootURL + '/' + $('#opinionId').val(),
		dataType : "json",
		data : formToJSON(),
		success : function(data, textStatus, jqXHR) {
			alert('Opinion updated successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('updateOpinion error: ' + textStatus);
		}
	});
}

// Delete Opinion
function deleteOpinion() {
	console.log('deleteOpinion');
	$.ajax({
		type : 'DELETE',
		url : rootURL + '/' + $('#opinionId').val(),
		success : function(data, textStatus, jqXHR) {
			alert('Opinion deleted successfully');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('deleteOpinion error');
		}
	});
}

// Utility functions
// ....

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an
	// object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$('#opinionList li').remove();
	$.each(list, function(index, opinion) {
		$('#opinionList').append(
				'<li><a href="#" data-identity="' + opinion.id + '">'
						+ opinion.name + '</a></li>');
	});
}

function renderDetails(opinion) {
	$('#opinionId').val(opinion.id);
	$('#name').val(opinion.name);
	$('#email').val(opinion.email);
	$('#expertise').val(opinion.expertise);
	$('#description').val(opinion.description);
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	var opinionId = $('#opinionId').val();
	return JSON.stringify({
		"id" : opinionId == "" ? null : opinionId,
		"name" : $('#name').val(),
		"email" : $('#email').val(),
		"expertise" : $('#expertise').val(),
		"description" : $('#description').val()
	});
}
