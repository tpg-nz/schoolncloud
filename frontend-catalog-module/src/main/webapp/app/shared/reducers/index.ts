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
import role, {
  RoleState
} from 'app/entities/role/role.reducer';
// prettier-ignore
import userRole, {
  UserRoleState
} from 'app/entities/user-role/user-role.reducer';
// prettier-ignore
import subject, {
  SubjectState
} from 'app/entities/subject/subject.reducer';
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
import paper, {
  PaperState
} from 'app/entities/paper/paper.reducer';
// prettier-ignore
import contact, {
  ContactState
} from 'app/entities/contact/contact.reducer';
// prettier-ignore
import address, {
  AddressState
} from 'app/entities/address/address.reducer';
// prettier-ignore
import educationalInstituition, {
  EducationalInstituitionState
} from 'app/entities/educational-instituition/educational-instituition.reducer';
// prettier-ignore
import campus, {
  CampusState
} from 'app/entities/campus/campus.reducer';
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
  readonly role: RoleState;
  readonly userRole: UserRoleState;
  readonly subject: SubjectState;
  readonly requirement: RequirementState;
  readonly qualification: QualificationState;
  readonly teachingStaff: TeachingStaffState;
  readonly teachingClass: TeachingClassState;
  readonly paper: PaperState;
  readonly contact: ContactState;
  readonly address: AddressState;
  readonly educationalInstituition: EducationalInstituitionState;
  readonly campus: CampusState;
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
  role,
  userRole,
  subject,
  requirement,
  qualification,
  teachingStaff,
  teachingClass,
  paper,
  contact,
  address,
  educationalInstituition,
  campus,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
