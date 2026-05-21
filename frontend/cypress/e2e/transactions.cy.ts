describe('Transacciones', () => {
  beforeEach(() => {
    cy.visit('http://localhost:5173/');
    cy.get('#email').type('sample@email.com');
    cy.get('#password').type('admin');

    // intercepts para simular backend
    cy.intercept('POST', '/auth/login', { statusCode: 200 }).as('login');
    cy.intercept('GET', '/auth/me', { fixture: 'user.json' }).as('me');

    cy.get('button[type="submit"]').click();
    cy.url({ timeout: 10000 }).should('include', '/app');
  });

  // Crear (pendiente hasta que el formulario esté implementado)
  it.skip('should create a new transaction', () => {
    // pendiente: implementar cuando el formulario esté listo
  });

  // Listar
  it('should list transactions', () => {
    cy.contains('Transacciones', { timeout: 10000 }).should('be.visible');
    cy.get('table').should('be.visible');
  });

  // Editar
  it('should edit a transaction', () => {
    cy.get('table tbody tr').first().within(() => {
      cy.contains('Editar').click();
    });
    // pendiente: ajustar selectores cuando el modal esté implementado
  });

  // Eliminar
  it('should delete a transaction', () => {
    cy.get('table tbody tr').first().within(() => {
      cy.contains('Eliminar').click();
    });
    cy.get('table tbody tr').should('have.length.lessThan', 1);
  });
});
