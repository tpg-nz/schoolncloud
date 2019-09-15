import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IContactUpdateState {
  isNew: boolean;
}

export class ContactUpdate extends React.Component<IContactUpdateProps, IContactUpdateState> {
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
      const { contactEntity } = this.props;
      const entity = {
        ...contactEntity,
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
    this.props.history.push('/entity/contact');
  };

  render() {
    const { contactEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="catalogApp.contact.home.createOrEditLabel">
              <Translate contentKey="catalogApp.contact.home.createOrEditLabel">Create or edit a Contact</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : contactEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="contact-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="contact-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guidLabel" for="contact-guid">
                    <Translate contentKey="catalogApp.contact.guid">Guid</Translate>
                  </Label>
                  <AvField
                    id="contact-guid"
                    type="text"
                    name="guid"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="entityGuidLabel" for="contact-entityGuid">
                    <Translate contentKey="catalogApp.contact.entityGuid">Entity Guid</Translate>
                  </Label>
                  <AvField id="contact-entityGuid" type="text" name="entityGuid" />
                </AvGroup>
                <AvGroup>
                  <Label id="contactLabel" for="contact-contact">
                    <Translate contentKey="catalogApp.contact.contact">Contact</Translate>
                  </Label>
                  <AvField id="contact-contact" type="text" name="contact" />
                </AvGroup>
                <AvGroup>
                  <Label id="contactTypeLabel" for="contact-contactType">
                    <Translate contentKey="catalogApp.contact.contactType">Contact Type</Translate>
                  </Label>
                  <AvInput
                    id="contact-contactType"
                    type="select"
                    className="form-control"
                    name="contactType"
                    value={(!isNew && contactEntity.contactType) || 'SUBJECT'}
                  >
                    <option value="SUBJECT">{translate('catalogApp.ContactType.SUBJECT')}</option>
                    <option value="INSTITUTIONAL">{translate('catalogApp.ContactType.INSTITUTIONAL')}</option>
                    <option value="CAMPUS">{translate('catalogApp.ContactType.CAMPUS')}</option>
                    <option value="TEACHING_STAFF">{translate('catalogApp.ContactType.TEACHING_STAFF')}</option>
                    <option value="PAPER">{translate('catalogApp.ContactType.PAPER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="mediaTypeLabel" for="contact-mediaType">
                    <Translate contentKey="catalogApp.contact.mediaType">Media Type</Translate>
                  </Label>
                  <AvInput
                    id="contact-mediaType"
                    type="select"
                    className="form-control"
                    name="mediaType"
                    value={(!isNew && contactEntity.mediaType) || 'EMAIL'}
                  >
                    <option value="EMAIL">{translate('catalogApp.MediaTypee.EMAIL')}</option>
                    <option value="WEB_SITE">{translate('catalogApp.MediaTypee.WEB_SITE')}</option>
                    <option value="MOBILE_PHONE">{translate('catalogApp.MediaTypee.MOBILE_PHONE')}</option>
                    <option value="PHONE">{translate('catalogApp.MediaTypee.PHONE')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/contact" replace color="info">
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
  contactEntity: storeState.contact.entity,
  loading: storeState.contact.loading,
  updating: storeState.contact.updating,
  updateSuccess: storeState.contact.updateSuccess
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
)(ContactUpdate);
