import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './campus.reducer';
import { ICampus } from 'app/shared/model/campus.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICampusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CampusDetail extends React.Component<ICampusDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { campusEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.campus.detail.title">Campus</Translate> [<b>{campusEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guid">
                <Translate contentKey="catalogApp.campus.guid">Guid</Translate>
              </span>
            </dt>
            <dd>{campusEntity.guid}</dd>
            <dt>
              <span id="name">
                <Translate contentKey="catalogApp.campus.name">Name</Translate>
              </span>
            </dt>
            <dd>{campusEntity.name}</dd>
            <dt>
              <Translate contentKey="catalogApp.campus.educationalInstitution">Educational Institution</Translate>
            </dt>
            <dd>{campusEntity.educationalInstitution ? campusEntity.educationalInstitution.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/campus" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/campus/${campusEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ campus }: IRootState) => ({
  campusEntity: campus.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CampusDetail);
