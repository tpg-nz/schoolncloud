export const enum AddressType {
  HOME = 'HOME',
  WORK = 'WORK',
  CORRESPONDENCE = 'CORRESPONDENCE'
}

export const enum ContactType {
  SUBJECT = 'SUBJECT',
  INSTITUTIONAL = 'INSTITUTIONAL',
  CAMPUS = 'CAMPUS',
  TEACHING_STAFF = 'TEACHING_STAFF',
  PAPER = 'PAPER'
}

export interface IAddress {
  id?: number;
  entityId?: number;
  address?: string;
  zipCode?: string;
  city?: string;
  country?: string;
  addressType?: AddressType;
  contactType?: ContactType;
}

export const defaultValue: Readonly<IAddress> = {};
