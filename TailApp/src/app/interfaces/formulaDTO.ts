export interface FormulaItemDTO {
    medicationName: string;
    quantity: string;
    instructions: string;
}

export interface FormulaRequestDTO {
    doctorCedula: string;
    patientCedula: string;
    appointmentId: string;
    diagnostic: string;
    formulaItems: FormulaItemDTO[];
}

export interface FormulaResponseDTO {
    idPrescription: string;
    doctorCedula: string;
    patientCedula: string;
    date: string; 
    diagnostic: string;
    items: FormulaItemDTO[];
}