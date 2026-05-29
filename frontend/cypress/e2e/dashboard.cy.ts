const authResponse = {
  userId: '11111111-1111-1111-1111-111111111111',
  email: 'ana@example.com',
  roles: ['ROLE_USER'],
  expiresAt: '2026-05-27T12:00:00',
};

const dashboardResponse = {
  userId: authResponse.userId,
  greetingName: 'Ana',
  totalBalance: 1200000,
  currency: 'COP',
  monthlyBalanceVariationPercent: 4.2,
  nextAlert: {
    id: '22222222-2222-2222-2222-222222222222',
    title: 'Alerta financiera',
    message: 'Pago cercano',
    date: '2026-05-30',
  },
  accounts: [
    {
      id: '33333333-3333-3333-3333-333333333333',
      name: 'Ahorros',
      subtitle: 'Cuenta de ahorros',
      balance: 1200000,
      accountType: 1,
      progressPercentage: null,
    },
  ],
  liquidity: {
    message: 'Flujo proyectado estable para los proximos 30 dias.',
    points: [{ date: '2026-05-30', projectedBalance: 1250000 }],
  },
  recentTransactions: [
    {
      id: '44444444-4444-4444-4444-444444444444',
      title: 'Nomina',
      subtitle: '27 may · Ahorros',
      amount: 500000,
      positive: true,
      date: '2026-05-27T09:00:00',
    },
  ],
};

const login = () => {
  cy.intercept('GET', '**/auth/csrf', {
    statusCode: 200,
    body: { token: 'test-csrf-token' },
  }).as('csrf');
  cy.intercept('POST', '**/auth/login', { statusCode: 200, body: authResponse }).as('login');
  cy.intercept('GET', '**/auth/me', { statusCode: 200, body: authResponse }).as('me');

  cy.visit('/');
  cy.get('#email').type(authResponse.email);
  cy.get('#password').type('admin');
  cy.get('button[type="submit"]').click();
  cy.wait('@csrf');
  cy.wait('@login');
  cy.wait('@me');
};

describe('Dashboard', () => {
  it('loads dashboard data from the API', () => {
    cy.intercept('GET', '**/api/dashboard', {
      statusCode: 200,
      delay: 250,
      body: dashboardResponse,
    }).as('dashboard');

    login();

    cy.get('[aria-label="Cargando dashboard"]').should('exist');
    cy.wait('@dashboard');
    cy.contains('Hola, Ana').should('be.visible');
    cy.contains('Ahorros').should('be.visible');
    cy.contains('Nomina').should('be.visible');
    cy.contains('Pago cercano').should('be.visible');
  });

  it('shows empty states when dashboard lists are empty', () => {
    cy.intercept('GET', '**/api/dashboard', {
      statusCode: 200,
      body: {
        ...dashboardResponse,
        nextAlert: null,
        accounts: [],
        liquidity: {
          message: 'Aun no hay proyecciones para los proximos 30 dias.',
          points: [],
        },
        recentTransactions: [],
      },
    }).as('dashboard');

    login();

    cy.wait('@dashboard');
    cy.contains('Aun no tienes cuentas registradas.').should('be.visible');
    cy.contains('Aun no tienes movimientos registrados.').should('be.visible');
    cy.contains('No tienes alertas financieras pendientes.').should('be.visible');
    cy.contains('Sin datos').should('be.visible');
  });
});
