import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EducationalInstitution from './educational-institution';
import EducationalInstitutionDetail from './educational-institution-detail';
import EducationalInstitutionUpdate from './educational-institution-update';
import EducationalInstitutionDeleteDialog from './educational-institution-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EducationalInstitutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EducationalInstitutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EducationalInstitutionDetail} />
      <ErrorBoundaryRoute path={match.url} component={EducationalInstitution} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EducationalInstitutionDeleteDialog} />
  </>
);

export default Routes;
