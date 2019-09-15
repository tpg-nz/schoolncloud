import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './paper.reducer';
import { IPaper } from 'app/shared/model/paper.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaperDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PaperDetail extends React.Component<IPaperDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { paperEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.paper.detail.title">Paper</Translate> [<b>{paperEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">
                <Translate contentKey="catalogApp.paper.code">Code</Translate>
              </span>
            </dt>
            <dd>{paperEntity.code}</dd>
            <dt>
              <span id="year">
                <Translate contentKey="catalogApp.paper.year">Year</Translate>
              </span>
            </dt>
            <dd>{paperEntity.year}</dd>
            <dt>
              <span id="points">
                <Translate contentKey="catalogApp.paper.points">Points</Translate>
              </span>
            </dt>
            <dd>{paperEntity.points}</dd>
            <dt>
              <span id="teachingPeriod">
                <Translate contentKey="catalogApp.paper.teachingPeriod">Teaching Period</Translate>
              </span>
            </dt>
            <dd>{paperEntity.teachingPeriod}</dd>
            <dt>
              <Translate contentKey="catalogApp.paper.subject">Subject</Translate>
            </dt>
            <dd>{paperEntity.subject ? paperEntity.subject.guid : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/paper" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/paper/${paperEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ paper }: IRootState) => ({
  paperEntity: paper.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaperDetail);
