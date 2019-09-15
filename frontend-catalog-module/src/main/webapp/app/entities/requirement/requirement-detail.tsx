import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './requirement.reducer';
import { IRequirement } from 'app/shared/model/requirement.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequirementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RequirementDetail extends React.Component<IRequirementDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { requirementEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.requirement.detail.title">Requirement</Translate> [<b>{requirementEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guid">
                <Translate contentKey="catalogApp.requirement.guid">Guid</Translate>
              </span>
            </dt>
            <dd>{requirementEntity.guid}</dd>
            <dt>
              <span id="level">
                <Translate contentKey="catalogApp.requirement.level">Level</Translate>
              </span>
            </dt>
            <dd>{requirementEntity.level}</dd>
            <dt>
              <Translate contentKey="catalogApp.requirement.subject">Subject</Translate>
            </dt>
            <dd>{requirementEntity.subject ? requirementEntity.subject.guid : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/requirement" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/requirement/${requirementEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ requirement }: IRootState) => ({
  requirementEntity: requirement.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequirementDetail);
