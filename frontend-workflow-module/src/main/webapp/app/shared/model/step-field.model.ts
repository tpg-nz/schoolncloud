import { IStep } from 'app/shared/model/step.model';

export const enum FieldType {
  TEXT_FIELD = 'TEXT_FIELD',
  TEXT_AREA = 'TEXT_AREA',
  COMBOBOX = 'COMBOBOX',
  CHECKBOX = 'CHECKBOX',
  RADIOBOX = 'RADIOBOX'
}

export interface IStepField {
  id?: number;
  sequence?: number;
  label?: string;
  fieldType?: FieldType;
  step?: IStep;
}

export const defaultValue: Readonly<IStepField> = {};
