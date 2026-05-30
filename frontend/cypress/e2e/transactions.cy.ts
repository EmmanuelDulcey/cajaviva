const authResponse = {
  userId: '11111111-1111-1111-1111-111111111111',
  email: 'sample@email.com',
  roles: ['ROLE_USER'],
  expiresAt: '2026-05-27T12:00:00',
};

const emptyDashboardResponse = {
  userId: authResponse.userId,
  greetingName: 'Sample',
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
};

describe('Transacciones', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
    cy.intercept('GET', '**/auth/csrf', {
      statusCode: 200,
      body: { token: 'test-csrf-token' },
    }).as('csrf');
    cy.intercept('POST', '**/auth/login', {
      statusCode: 200,
      body: authResponse,
    }).as('login');
    cy.intercept('GET', '**/auth/me', {
      statusCode: 200,
      body: authResponse,
    }).as('me');
    cy.intercept('GET', '**/api/dashboard', {
      statusCode: 200,
      body: emptyDashboardResponse,
    }).as('dashboard');

    cy.visit('/');
    cy.get('#email').type(authResponse.email);
    cy.get('#password').type('admin');
    cy.get('button[type="submit"]').click();

    cy.wait('@csrf');
    cy.wait('@login');
    cy.wait('@me');
    cy.url({ timeout: 10000 }).should('include', '/app');
  });

  it('should show the transactions navigation entry', () => {
    cy.contains('button', 'Transacciones').should('be.visible');
  });

  it.skip('should list transactions', () => {
    cy.contains('button', 'Transacciones').click();
    cy.get('table').should('be.visible');
  });

  it.skip('should create a new transaction', () => {
    // Pendiente hasta implementar el formulario de transacciones.
  });

  it.skip('should edit a transaction', () => {
    // Pendiente hasta implementar tabla y acciones de transacciones.
  });

  it.skip('should delete a transaction', () => {
    // Pendiente hasta implementar tabla y acciones de transacciones.
  });
});
