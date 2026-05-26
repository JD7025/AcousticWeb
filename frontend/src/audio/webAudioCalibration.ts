import type { AmbientNoiseResult, FrequencyPoint, MeasurementResult } from "../types/Audio";

export class WebAudioCalibration {
  private audioContext: AudioContext | null = null;
  private analyser: AnalyserNode | null = null;
  private microphoneStream: MediaStream | null = null;
  private sourceNode: MediaStreamAudioSourceNode | null = null;

  async initializeMicrophone(): Promise<void> {
    this.microphoneStream = await navigator.mediaDevices.getUserMedia({
      audio: {
        echoCancellation: false,
        noiseSuppression: false,
        autoGainControl: false,
      },
    });

    this.audioContext = new AudioContext();
    this.analyser = this.audioContext.createAnalyser();
    this.analyser.fftSize = 4096;
    this.analyser.smoothingTimeConstant = 0.8;

    this.sourceNode = this.audioContext.createMediaStreamSource(this.microphoneStream);
    this.sourceNode.connect(this.analyser);
  }

  async measureAmbientNoise(durationMs = 3000): Promise<AmbientNoiseResult> {
    this.ensureReady();

    const analyser = this.analyser!;
    const data = new Float32Array(analyser.frequencyBinCount);
    const samples: number[] = [];

    const startedAt = performance.now();

    while (performance.now() - startedAt < durationMs) {
      analyser.getFloatFrequencyData(data);

      const average = this.averageDb(data);
      samples.push(average);

      await this.sleep(120);
    }

    return {
      promedioDb: this.round(this.average(samples), 2),
      muestras: samples.map((value) => this.round(value, 2)),
    };
  }

  async runSweepAndMeasure(durationMs = 6000): Promise<MeasurementResult> {
    this.ensureReady();

    const oscillatorContext = this.audioContext!;
    const oscillator = oscillatorContext.createOscillator();
    const gain = oscillatorContext.createGain();

    oscillator.type = "sine";

    const now = oscillatorContext.currentTime;
    const durationSeconds = durationMs / 1000;

    gain.gain.setValueAtTime(0.0001, now);
    gain.gain.exponentialRampToValueAtTime(0.2, now + 0.5);
    gain.gain.setValueAtTime(0.2, now + durationSeconds - 0.5);
    gain.gain.exponentialRampToValueAtTime(0.0001, now + durationSeconds);

    oscillator.frequency.setValueAtTime(40, now);
    oscillator.frequency.exponentialRampToValueAtTime(16000, now + durationSeconds);

    oscillator.connect(gain);
    gain.connect(oscillatorContext.destination);

    oscillator.start(now);
    oscillator.stop(now + durationSeconds);

    const points = await this.captureFrequencyResponse(durationMs);

    return {
      puntos: points,
      promedioDb: this.round(this.average(points.map((p) => p.nivelDb)), 2),
    };
  }

  stop(): void {
    if (this.microphoneStream) {
      this.microphoneStream.getTracks().forEach((track) => track.stop());
    }

    if (this.audioContext && this.audioContext.state !== "closed") {
      this.audioContext.close();
    }

    this.audioContext = null;
    this.analyser = null;
    this.microphoneStream = null;
    this.sourceNode = null;
  }

  private async captureFrequencyResponse(durationMs: number): Promise<FrequencyPoint[]> {
    const analyser = this.analyser!;
    const audioContext = this.audioContext!;
    const data = new Float32Array(analyser.frequencyBinCount);

    const startedAt = performance.now();
    const accumulated = new Map<number, number[]>();

    while (performance.now() - startedAt < durationMs) {
      analyser.getFloatFrequencyData(data);

      const points = this.convertBinsToFrequencyPoints(data, audioContext.sampleRate);

      for (const point of points) {
        if (!accumulated.has(point.frecuenciaHz)) {
          accumulated.set(point.frecuenciaHz, []);
        }

        accumulated.get(point.frecuenciaHz)!.push(point.nivelDb);
      }

      await this.sleep(120);
    }

    return Array.from(accumulated.entries())
      .map(([frequency, values]) => ({
        frecuenciaHz: frequency,
        nivelDb: this.round(this.average(values), 2),
        faseGrados: null,
      }))
      .filter((point) => point.frecuenciaHz >= 40 && point.frecuenciaHz <= 16000)
      .sort((a, b) => a.frecuenciaHz - b.frecuenciaHz);
  }

  private convertBinsToFrequencyPoints(data: Float32Array, sampleRate: number): FrequencyPoint[] {
    const targetFrequencies = [
      40, 50, 63, 80, 100, 125, 160, 200, 250, 315,
      400, 500, 630, 800, 1000, 1250, 1600, 2000,
      2500, 3150, 4000, 5000, 6300, 8000, 10000,
      12500, 16000,
    ];

    const binResolution = sampleRate / (data.length * 2);

    return targetFrequencies.map((frequency) => {
      const binIndex = Math.round(frequency / binResolution);
      const safeIndex = Math.min(Math.max(binIndex, 0), data.length - 1);
      const db = data[safeIndex];

      return {
        frecuenciaHz: frequency,
        nivelDb: this.normalizeDb(db),
        faseGrados: null,
      };
    });
  }

  private normalizeDb(rawDb: number): number {
    if (!Number.isFinite(rawDb)) {
      return 0;
    }

    return Math.max(0, Math.min(100, rawDb + 100));
  }

  private averageDb(data: Float32Array): number {
    const validValues = Array.from(data)
      .filter((value) => Number.isFinite(value))
      .map((value) => this.normalizeDb(value));

    return this.average(validValues);
  }

  private average(values: number[]): number {
    if (values.length === 0) return 0;

    const total = values.reduce((sum, value) => sum + value, 0);
    return total / values.length;
  }

  private round(value: number, decimals: number): number {
    return Number(value.toFixed(decimals));
  }

  private sleep(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  private ensureReady(): void {
    if (!this.audioContext || !this.analyser || !this.microphoneStream) {
      throw new Error("El micrófono no ha sido inicializado.");
    }
  }
}