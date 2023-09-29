const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    // Better done in cypress.env.json
    baseUrl: 'https://example.cypress.io',  
  },
});
