# TenisuFront

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 20.3.7.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests (Cypress)

This project uses Cypress for e2e tests.

Prerequisites:
- Start the application in another terminal:
  ```bash
  npm start
  # or: ng serve
  ```
  The tests expect the app at http://localhost:4200

Open Cypress in interactive mode:
```bash
npm run e2e
```

Run Cypress headlessly (CI-friendly):
```bash
npm run e2e:run
```

Notes:
- Tests stub backend calls to `http://localhost:8080/apis` using fixtures under `cypress/fixtures` via `cy.intercept`.
- Initial specs live in `cypress/e2e/`. See `home.cy.ts`.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
