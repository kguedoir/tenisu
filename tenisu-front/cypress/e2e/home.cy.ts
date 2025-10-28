/// <reference types="cypress" />

describe('Home page & navigation', () => {
  const apiBase = 'http://localhost:8080/apis';

  beforeEach(() => {
    // Stub the search endpoint by default with a list of players
    cy.intercept('GET', `${apiBase}/players*`, { fixture: 'players.json' }).as('searchPlayers');
  });

  it('loads the home page and displays the search bar and player list', () => {
    cy.visit('/');

    // Search input is visible
    cy.get('input[name="playerSearch"]').should('be.visible').and('have.attr', 'placeholder', 'Rechercher un joueur');

    // Wait for initial search triggered on ngOnInit
    cy.wait('@searchPlayers');

    // Ensure some player cards are rendered
    cy.get('.player-list app-player-card').should('have.length.at.least', 1);

    // And the button inside the card has accessible label
    cy.get('.player-list app-player-card .player-card')
      .first()
      .should('have.attr', 'aria-label')
      .and('match', /Voir les détails pour/);
  });

  it('navigates to player detail when clicking a player card', () => {
    // For detail route, stub the player by id endpoint
    cy.fixture('player-1.json').then((player) => {
      cy.intercept('GET', `${apiBase}/players/${player.id}`, { fixture: 'player-1.json' }).as('getPlayer1');
    });

    cy.visit('/');
    cy.wait('@searchPlayers');

    // Click the first player card
    cy.get('.player-list app-player-card .player-card').first().click();

    // URL should include /player/<id>
    cy.url().should('match', /\/player\/\d+$/);

    // Player detail view should load and display last name element (fixture uses Rafael NADAL)
    cy.wait('@getPlayer1');
    cy.contains('.lastname', /NADAL/i).should('be.visible');

    // Back button should exist
    cy.get('button.close-btn').should('be.visible');
  });
});
