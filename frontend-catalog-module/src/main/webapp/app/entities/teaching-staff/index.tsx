import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TeachingStaff from './teaching-staff';
import TeachingStaffDetail from './teaching-staff-detail';
import TeachingStaffUpdate from './teaching-staff-update';
import TeachingStaffDeleteDialog from './teaching-staff-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TeachingStaffUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TeachingStaffUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TeachingStaffDetail} />
      <ErrorBoundaryRoute path={match.url} component={TeachingStaff} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TeachingStaffDeleteDialog} />
  </>
);

export default Routes;
