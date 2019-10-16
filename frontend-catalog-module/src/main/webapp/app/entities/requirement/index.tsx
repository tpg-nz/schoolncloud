import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Requirement from './requirement';
import RequirementDetail from './requirement-detail';
import RequirementUpdate from './requirement-update';
import RequirementDeleteDialog from './requirement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RequirementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RequirementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RequirementDetail} />
      <ErrorBoundaryRoute path={match.url} component={Requirement} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RequirementDeleteDialog} />
  </>
);

export default Routes;
