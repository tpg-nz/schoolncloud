import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EducationalInstituition from './educational-instituition';
import EducationalInstituitionDetail from './educational-instituition-detail';
import EducationalInstituitionUpdate from './educational-instituition-update';
import EducationalInstituitionDeleteDialog from './educational-instituition-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EducationalInstituitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EducationalInstituitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EducationalInstituitionDetail} />
      <ErrorBoundaryRoute path={match.url} component={EducationalInstituition} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EducationalInstituitionDeleteDialog} />
  </>
);

export default Routes;
