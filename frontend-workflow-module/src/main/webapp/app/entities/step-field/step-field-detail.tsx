import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './step-field.reducer';
import { IStepField } from 'app/shared/model/step-field.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStepFieldDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StepFieldDetail extends React.Component<IStepFieldDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { stepFieldEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="workflowApp.stepField.detail.title">StepField</Translate> [<b>{stepFieldEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="sequence">
                <Translate contentKey="workflowApp.stepField.sequence">Sequence</Translate>
              </span>
            </dt>
            <dd>{stepFieldEntity.sequence}</dd>
            <dt>
              <span id="label">
                <Translate contentKey="workflowApp.stepField.label">Label</Translate>
              </span>
            </dt>
            <dd>{stepFieldEntity.label}</dd>
            <dt>
              <span id="fieldType">
                <Translate contentKey="workflowApp.stepField.fieldType">Field Type</Translate>
              </span>
            </dt>
            <dd>{stepFieldEntity.fieldType}</dd>
            <dt>
              <Translate contentKey="workflowApp.stepField.step">Step</Translate>
            </dt>
            <dd>{stepFieldEntity.step ? stepFieldEntity.step.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/step-field" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/step-field/${stepFieldEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ stepField }: IRootState) => ({
  stepFieldEntity: stepField.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StepFieldDetail);
