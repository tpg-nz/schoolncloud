import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './teaching-class.reducer';
import { ITeachingClass } from 'app/shared/model/teaching-class.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeachingClassDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TeachingClassDetail extends React.Component<ITeachingClassDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { teachingClassEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.teachingClass.detail.title">TeachingClass</Translate> [<b>{teachingClassEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">
                <Translate contentKey="catalogApp.teachingClass.code">Code</Translate>
              </span>
            </dt>
            <dd>{teachingClassEntity.code}</dd>
            <dt>
              <span id="year">
                <Translate contentKey="catalogApp.teachingClass.year">Year</Translate>
              </span>
            </dt>
            <dd>{teachingClassEntity.year}</dd>
            <dt>
              <span id="semester">
                <Translate contentKey="catalogApp.teachingClass.semester">Semester</Translate>
              </span>
            </dt>
            <dd>{teachingClassEntity.semester}</dd>
            <dt>
              <Translate contentKey="catalogApp.teachingClass.campus">Campus</Translate>
            </dt>
            <dd>{teachingClassEntity.campus ? teachingClassEntity.campus.name : ''}</dd>
            <dt>
              <Translate contentKey="catalogApp.teachingClass.paper">Paper</Translate>
            </dt>
            <dd>{teachingClassEntity.paper ? teachingClassEntity.paper.code : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/teaching-class" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/teaching-class/${teachingClassEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ teachingClass }: IRootState) => ({
  teachingClassEntity: teachingClass.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeachingClassDetail);
