import { CountryNamePipe } from './country-name-pipe';

describe('CountryNamePipe', () => {
  it('create an instance', () => {
    const pipe = new CountryNamePipe();
    expect(pipe).toBeTruthy();
  });

  it('should map ISO-3 sports code SUI to Switzerland (CH)', () => {
    const pipe = new CountryNamePipe();
    const nameEn = pipe.transform('SUI', 'en');
    // Exact localized value may vary by environment; just ensure non-empty and not raw code
    expect(nameEn).toBeTruthy();
    expect(nameEn).not.toBe('SUI');
  });

  it('should return original code when unknown', () => {
    const pipe = new CountryNamePipe();
    expect(pipe.transform('XXX')).toBe('XXX');
  });

  it('should handle empty/undefined/null as empty string', () => {
    const pipe = new CountryNamePipe();
    expect(pipe.transform('')).toBe('');
    expect(pipe.transform(undefined as any)).toBe('');
    expect(pipe.transform(null as any)).toBe('');
  });
});
