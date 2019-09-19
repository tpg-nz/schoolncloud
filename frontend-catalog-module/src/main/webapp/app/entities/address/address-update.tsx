import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './address.reducer';
import { IAddress } from 'app/shared/model/address.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAddressUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAddressUpdateState {
  isNew: boolean;
}

export class AddressUpdate extends React.Component<IAddressUpdateProps, IAddressUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { addressEntity } = this.props;
      const entity = {
        ...addressEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/address');
  };

  render() {
    const { addressEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.address.home.createOrEditLabel">
              <Translate contentKey="catalogApp.address.home.createOrEditLabel">Create or edit a Address</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : addressEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="address-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="address-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="entityIdLabel" for="address-entityId">
                    <Translate contentKey="catalogApp.address.entityId">Entity Id</Translate>
                  </Label>
                  <AvField
                    id="address-entityId"
                    type="string"
                    className="form-control"
                    name="entityId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="address-address">
                    <Translate contentKey="catalogApp.address.address">Address</Translate>
                  </Label>
                  <AvField
                    id="address-address"
                    type="text"
                    name="address"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="zipCodeLabel" for="address-zipCode">
                    <Translate contentKey="catalogApp.address.zipCode">Zip Code</Translate>
                  </Label>
                  <AvField
                    id="address-zipCode"
                    type="text"
                    name="zipCode"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="address-city">
                    <Translate contentKey="catalogApp.address.city">City</Translate>
                  </Label>
                  <AvField
                    id="address-city"
                    type="text"
                    name="city"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="address-country">
                    <Translate contentKey="catalogApp.address.country">Country</Translate>
                  </Label>
                  <AvField
                    id="address-country"
                    type="text"
                    name="country"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressTypeLabel" for="address-addressType">
                    <Translate contentKey="catalogApp.address.addressType">Address Type</Translate>
                  </Label>
                  <AvInput
                    id="address-addressType"
                    type="select"
                    className="form-control"
                    name="addressType"
                    value={(!isNew && addressEntity.addressType) || 'HOME'}
                  >
                    <option value="HOME">{translate('catalogApp.AddressType.HOME')}</option>
                    <option value="WORK">{translate('catalogApp.AddressType.WORK')}</option>
                    <option value="CORRESPONDENCE">{translate('catalogApp.AddressType.CORRESPONDENCE')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="contactTypeLabel" for="address-contactType">
                    <Translate contentKey="catalogApp.address.contactType">Contact Type</Translate>
                  </Label>
                  <AvInput
                    id="address-contactType"
                    type="select"
                    className="form-control"
                    name="contactType"
                    value={(!isNew && addressEntity.contactType) || 'SUBJECT'}
                  >
                    <option value="SUBJECT">{translate('catalogApp.ContactType.SUBJECT')}</option>
                    <option value="INSTITUTIONAL">{translate('catalogApp.ContactType.INSTITUTIONAL')}</option>
                    <option value="CAMPUS">{translate('catalogApp.ContactType.CAMPUS')}</option>
                    <option value="TEACHING_STAFF">{translate('catalogApp.ContactType.TEACHING_STAFF')}</option>
                    <option value="PAPER">{translate('catalogApp.ContactType.PAPER')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/address" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  addressEntity: storeState.address.entity,
  loading: storeState.address.loading,
  updating: storeState.address.updating,
  updateSuccess: storeState.address.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddressUpdate);
