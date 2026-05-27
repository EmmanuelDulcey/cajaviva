describe('Login flow', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('should log in with valid credentials', () => {
    cy.intercept('GET', '**/auth/csrf', {
      statusCode: 200,
      body: { token: 'test-csrf-token' },
    }).as('csrf');

    cy.intercept('POST', '**/auth/login', {
      statusCode: 200,
      body: {
        userId: '11111111-1111-1111-1111-111111111111',
        email: 'sample@email.com',
        roles: ['ROLE_USER'],
        expiresAt: '2026-05-27T12:00:00',
      },
    }).as('login');
    cy.intercept('GET', '**/auth/me', {
      statusCode: 200,
      body: {
        userId: '11111111-1111-1111-1111-111111111111',
        email: 'sample@email.com',
        roles: ['ROLE_USER'],
        expiresAt: '2026-05-27T12:00:00',
      },
    }).as('me');

    cy.intercept('GET', '**/api/dashboard', {
      statusCode: 200,
      body: {
        userId: '11111111-1111-1111-1111-111111111111',
        greetingName: 'Test',
        totalBalance: 0,
        currency: 'COP',
        monthlyBalanceVariationPercent: 0,
        nextAlert: null,
        accounts: [],
        liquidity: {
          message: 'Aun no hay proyecciones para los proximos 30 dias.',
          points: [],
        },
        recentTransactions: [],
      },
    });

    cy.visit('/');

    cy.get('#email').type('sample@email.com');
    cy.get('#password').type('admin');
    cy.get('button[type="submit"]').click();

    cy.wait('@csrf');
    cy.wait('@login');
    cy.wait('@me');
    cy.url().should('include', '/app');
    cy.contains('Inicio').should('be.visible');
  });
});
