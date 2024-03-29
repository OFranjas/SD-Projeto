// Add an event listener to the menu items
document.addEventListener("DOMContentLoaded", function () {
	var menuItems = document.querySelectorAll("#menu li a");
	for (var i = 0; i < menuItems.length; i++) {
		menuItems[i].addEventListener("click", function () {
			// Add your logic here for handling menu item clicks
			console.log("Menu item clicked: " + this.innerHTML);

			// Redirect to the page for the clicked menu item

			// Get the page name from the menu item
			var pageName = this.innerHTML.toLowerCase();

			// Redirect to the page
			window.location.href = "/" + pageName;
		});
	}
});
