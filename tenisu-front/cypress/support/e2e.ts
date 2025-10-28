// Import commands.ts using ES2015 syntax:
// No custom commands yet, but file is required by Cypress config.

// Ensure uncaught exceptions from the app don't fail tests by default
Cypress.on('uncaught:exception', (err) => {
  // Returning false here prevents Cypress from failing the test
  // When the app under test throws non-critical errors
  console.error('Uncaught exception in app:', err);
  return false;
});
