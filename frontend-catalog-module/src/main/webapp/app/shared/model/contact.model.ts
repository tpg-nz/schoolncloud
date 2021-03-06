export const enum ContactType {
  SUBJECT = 'SUBJECT',
  INSTITUTIONAL = 'INSTITUTIONAL',
  CAMPUS = 'CAMPUS',
  TEACHING_STAFF = 'TEACHING_STAFF',
  PAPER = 'PAPER'
}

export const enum MediaType {
  EMAIL = 'EMAIL',
  WEB_SITE = 'WEB_SITE',
  MOBILE_PHONE = 'MOBILE_PHONE',
  PHONE = 'PHONE'
}

export interface IContact {
  id?: number;
  entityId?: number;
  contact?: string;
  contactType?: ContactType;
  mediaType?: MediaType;
}

export const defaultValue: Readonly<IContact> = {};
