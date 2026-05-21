describe('Login flow', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('should log in with valid credentials', () => {
    cy.intercept('POST', '/auth/login', {
      statusCode: 200,
      body: { email: 'sample@email.com', name: 'Test User' },
    });

    cy.visit('http://localhost:5173/');

    cy.get('#email').type('sample@email.com');
    cy.get('#password').type('admin');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/app');
    cy.contains('Inicio').should('be.visible');
  });
});
