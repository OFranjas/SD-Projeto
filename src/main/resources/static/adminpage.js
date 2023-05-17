var stompClient = null;

var count = 0;

// Boolean connected = false;

// On page load, run update()
$(document).ready(function () {
	// updatePage();

	connect();
});

// Update the page when a new message is received, using AJAX
function updatePage() {
	$.ajax({
		url: "/topic/messages",
		type: "GET",
		success: function (result) {
			$("#greetings").html(result);
			// Only print the last received message
		},
		error: function (result) {
			console.log("Error");
		},
	});
}

function connect() {
	var socket = new SockJS("/my-websocket");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		setConnected(true);
		console.log("Connected: " + frame);

		// $.ajax({
		// 	url: "/topic/messages",
		// 	type: "GET",
		// 	success: function (result) {
		// 		$("#greetings").html(result);
		// 		// Only print the last received message
		// 	},
		// 	error: function (result) {
		// 		console.log("Error");
		// 	},
		// });

		// Q: How to get the last message from the server?
		// A: Use a counter to check if the page has been refreshed

		stompClient.subscribe("/topic/messages", function (greeting) {
			if (count != 0) {
				$("#greetings").html("");
			} else {
				count++;
			}
			showGreeting(JSON.parse(greeting.body).text);
		});
	});
}

function checkConnection() {
	if (stompClient !== null) {
		return true;
	}
	return false;
}

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	// Refreshes the page
	// location.reload();

	stompClient.send(
		"/app/messages",
		{},
		JSON.stringify({ name: $("#name").val() })
	);
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
	$("form").on("submit", function (e) {
		e.preventDefault();
	});
	$("#connect").click(function () {
		connect();
	});
	$("#disconnect").click(function () {
		disconnect();
	});
	$("#send").click(function () {
		sendName();
	});
});
