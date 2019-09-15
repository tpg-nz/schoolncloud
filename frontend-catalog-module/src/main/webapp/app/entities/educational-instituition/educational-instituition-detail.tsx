import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './educational-instituition.reducer';
import { IEducationalInstituition } from 'app/shared/model/educational-instituition.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEducationalInstituitionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EducationalInstituitionDetail extends React.Component<IEducationalInstituitionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { educationalInstituitionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.educationalInstituition.detail.title">EducationalInstituition</Translate> [
            <b>{educationalInstituitionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guid">
                <Translate contentKey="catalogApp.educationalInstituition.guid">Guid</Translate>
              </span>
            </dt>
            <dd>{educationalInstituitionEntity.guid}</dd>
            <dt>
              <span id="name">
                <Translate contentKey="catalogApp.educationalInstituition.name">Name</Translate>
              </span>
            </dt>
            <dd>{educationalInstituitionEntity.name}</dd>
          </dl>
          <Button tag={Link} to="/entity/educational-instituition" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/educational-instituition/${educationalInstituitionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ educationalInstituition }: IRootState) => ({
  educationalInstituitionEntity: educationalInstituition.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EducationalInstituitionDetail);
