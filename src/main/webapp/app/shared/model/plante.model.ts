import { ITypePlant } from '@/shared/model/type-plant.model';
import { IPlantage } from '@/shared/model/plantage.model';

export interface IPlante {
  id?: number;
  lebelle?: string;
  photo?: string | null;
  racin?: string | null;
  typePlant?: ITypePlant | null;
  plantages?: IPlantage[] | null;
}

export class Plante implements IPlante {
  constructor(
    public id?: number,
    public lebelle?: string,
    public photo?: string | null,
    public racin?: string | null,
    public typePlant?: ITypePlant | null,
    public plantages?: IPlantage[] | null
  ) {}
}
