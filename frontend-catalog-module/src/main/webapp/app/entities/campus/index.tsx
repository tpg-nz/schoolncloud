import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Campus from './campus';
import CampusDetail from './campus-detail';
import CampusUpdate from './campus-update';
import CampusDeleteDialog from './campus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CampusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CampusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CampusDetail} />
      <ErrorBoundaryRoute path={match.url} component={Campus} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CampusDeleteDialog} />
  </>
);

export default Routes;
