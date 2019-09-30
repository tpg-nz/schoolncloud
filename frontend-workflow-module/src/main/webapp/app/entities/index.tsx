import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Workflow from './workflow';
import Step from './step';
import StepField from './step-field';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/workflow`} component={Workflow} />
      <ErrorBoundaryRoute path={`${match.url}/step`} component={Step} />
      <ErrorBoundaryRoute path={`${match.url}/step-field`} component={StepField} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
