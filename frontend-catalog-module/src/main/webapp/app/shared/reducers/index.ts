import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import subject, {
  SubjectState
} from 'app/entities/subject/subject.reducer';
// prettier-ignore
import paper, {
  PaperState
} from 'app/entities/paper/paper.reducer';
// prettier-ignore
import requirement, {
  RequirementState
} from 'app/entities/requirement/requirement.reducer';
// prettier-ignore
import qualification, {
  QualificationState
} from 'app/entities/qualification/qualification.reducer';
// prettier-ignore
import teachingStaff, {
  TeachingStaffState
} from 'app/entities/teaching-staff/teaching-staff.reducer';
// prettier-ignore
import teachingClass, {
  TeachingClassState
} from 'app/entities/teaching-class/teaching-class.reducer';
// prettier-ignore
import educationalInstitution, {
  EducationalInstitutionState
} from 'app/entities/educational-institution/educational-institution.reducer';
// prettier-ignore
import campus, {
  CampusState
} from 'app/entities/campus/campus.reducer';
// prettier-ignore
import contact, {
  ContactState
} from 'app/entities/contact/contact.reducer';
// prettier-ignore
import address, {
  AddressState
} from 'app/entities/address/address.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly subject: SubjectState;
  readonly paper: PaperState;
  readonly requirement: RequirementState;
  readonly qualification: QualificationState;
  readonly teachingStaff: TeachingStaffState;
  readonly teachingClass: TeachingClassState;
  readonly educationalInstitution: EducationalInstitutionState;
  readonly campus: CampusState;
  readonly contact: ContactState;
  readonly address: AddressState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  subject,
  paper,
  requirement,
  qualification,
  teachingStaff,
  teachingClass,
  educationalInstitution,
  campus,
  contact,
  address,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
