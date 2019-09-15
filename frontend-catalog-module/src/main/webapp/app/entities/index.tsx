import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Role from './role';
import UserRole from './user-role';
import Subject from './subject';
import Requirement from './requirement';
import Qualification from './qualification';
import TeachingStaff from './teaching-staff';
import TeachingClass from './teaching-class';
import Paper from './paper';
import Contact from './contact';
import Address from './address';
import EducationalInstituition from './educational-instituition';
import Campus from './campus';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/role`} component={Role} />
      <ErrorBoundaryRoute path={`${match.url}/user-role`} component={UserRole} />
      <ErrorBoundaryRoute path={`${match.url}/subject`} component={Subject} />
      <ErrorBoundaryRoute path={`${match.url}/requirement`} component={Requirement} />
      <ErrorBoundaryRoute path={`${match.url}/qualification`} component={Qualification} />
      <ErrorBoundaryRoute path={`${match.url}/teaching-staff`} component={TeachingStaff} />
      <ErrorBoundaryRoute path={`${match.url}/teaching-class`} component={TeachingClass} />
      <ErrorBoundaryRoute path={`${match.url}/paper`} component={Paper} />
      <ErrorBoundaryRoute path={`${match.url}/contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}/address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}/educational-instituition`} component={EducationalInstituition} />
      <ErrorBoundaryRoute path={`${match.url}/campus`} component={Campus} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
