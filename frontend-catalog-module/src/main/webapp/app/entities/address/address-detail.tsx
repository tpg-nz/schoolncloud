import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './address.reducer';
import { IAddress } from 'app/shared/model/address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAddressDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AddressDetail extends React.Component<IAddressDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { addressEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.address.detail.title">Address</Translate> [<b>{addressEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="entityId">
                <Translate contentKey="catalogApp.address.entityId">Entity Id</Translate>
              </span>
            </dt>
            <dd>{addressEntity.entityId}</dd>
            <dt>
              <span id="address">
                <Translate contentKey="catalogApp.address.address">Address</Translate>
              </span>
            </dt>
            <dd>{addressEntity.address}</dd>
            <dt>
              <span id="zipCode">
                <Translate contentKey="catalogApp.address.zipCode">Zip Code</Translate>
              </span>
            </dt>
            <dd>{addressEntity.zipCode}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="catalogApp.address.city">City</Translate>
              </span>
            </dt>
            <dd>{addressEntity.city}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="catalogApp.address.country">Country</Translate>
              </span>
            </dt>
            <dd>{addressEntity.country}</dd>
            <dt>
              <span id="addressType">
                <Translate contentKey="catalogApp.address.addressType">Address Type</Translate>
              </span>
            </dt>
            <dd>{addressEntity.addressType}</dd>
            <dt>
              <span id="contactType">
                <Translate contentKey="catalogApp.address.contactType">Contact Type</Translate>
              </span>
            </dt>
            <dd>{addressEntity.contactType}</dd>
          </dl>
          <Button tag={Link} to="/entity/address" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/address/${addressEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ address }: IRootState) => ({
  addressEntity: address.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddressDetail);
