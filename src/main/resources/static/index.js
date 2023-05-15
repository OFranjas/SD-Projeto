window.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector('form');
    const linkInput = document.getElementById('link');
  
    form.addEventListener('submit', (event) => {
      event.preventDefault();
      const link = linkInput.value;
      console.log('Link submitted:', link);
      // You can perform further actions with the link here
    });
  });
  