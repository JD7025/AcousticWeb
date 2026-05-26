export interface FrequencyPoint {
  frecuenciaHz: number;
  nivelDb: number;
  faseGrados?: number | null;
}

export interface AmbientNoiseResult {
  promedioDb: number;
  muestras: number[];
}

export interface MeasurementResult {
  puntos: FrequencyPoint[];
  promedioDb: number;
}