import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserRole from './user-role';
import UserRoleDetail from './user-role-detail';
import UserRoleUpdate from './user-role-update';
import UserRoleDeleteDialog from './user-role-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserRoleDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserRole} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UserRoleDeleteDialog} />
  </>
);

export default Routes;
