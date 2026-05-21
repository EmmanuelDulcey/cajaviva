// cypress/e2e/transactions.cy.ts

describe('Transacciones', () => {
  beforeEach(() => {
    cy.visit('http://localhost:5173/');
    cy.get('#email').type('sample@email.com');
    cy.get('#password').type('admin');

    // intercepts para simular backend
    cy.intercept('POST', '/auth/login', { statusCode: 200 }).as('login');
    cy.intercept('GET', '/auth/me', { fixture: 'user.json' }).as('me');

    cy.get('button[type="submit"]').click();

    // esperar redirección al dashboard
    cy.url({ timeout: 10000 }).should('include', '/app');
  });

  it('should create a new transaction', () => {
    cy.contains('Nueva Transaccion').click();

    // completar formulario (ajusta selectores según tu implementación)
    cy.get('input[name="descripcion"]').type('Compra de prueba');
    cy.get('input[name="monto"]').type('1000');
    cy.get('select[name="categoria"]').select('Alimentación');

    cy.get('button[type="submit"]').click();

    // validar que aparece en la lista
    cy.contains('Compra de prueba').should('be.visible');
    cy.contains('1000').should('be.visible');
  });
});
