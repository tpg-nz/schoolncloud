import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContactDetail extends React.Component<IContactDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contactEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.contact.detail.title">Contact</Translate> [<b>{contactEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guid">
                <Translate contentKey="catalogApp.contact.guid">Guid</Translate>
              </span>
            </dt>
            <dd>{contactEntity.guid}</dd>
            <dt>
              <span id="entityGuid">
                <Translate contentKey="catalogApp.contact.entityGuid">Entity Guid</Translate>
              </span>
            </dt>
            <dd>{contactEntity.entityGuid}</dd>
            <dt>
              <span id="contact">
                <Translate contentKey="catalogApp.contact.contact">Contact</Translate>
              </span>
            </dt>
            <dd>{contactEntity.contact}</dd>
            <dt>
              <span id="contactType">
                <Translate contentKey="catalogApp.contact.contactType">Contact Type</Translate>
              </span>
            </dt>
            <dd>{contactEntity.contactType}</dd>
            <dt>
              <span id="mediaType">
                <Translate contentKey="catalogApp.contact.mediaType">Media Type</Translate>
              </span>
            </dt>
            <dd>{contactEntity.mediaType}</dd>
          </dl>
          <Button tag={Link} to="/entity/contact" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/contact/${contactEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ contact }: IRootState) => ({
  contactEntity: contact.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactDetail);
