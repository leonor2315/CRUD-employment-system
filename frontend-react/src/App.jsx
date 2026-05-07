import { useEffect, useRef, useState } from "react";

// Backend endpoints used by the dashboard and analytics cards.
const JAVA_API = "http://127.0.0.1:8080/api/admin/employee-work";
const PYTHON_API = "http://127.0.0.1:8000/api/analytics/work-summary";
// Local storage keys for remembering form layout/custom fields between sessions.
const ORDER_KEY = "employee-form-order-v1";
const CUSTOM_KEY = "employee-custom-fields-v1";
const THEME_KEY = "employee-ui-theme-v1";
const AUTH_SESSION_KEY = "employee-auth-session-v1";
const LOCATION_OPTIONS_KEY = "employee-location-options-v1";
const CHECKLIST_OPTIONS = ["Cellphone", "Laptop", "Car", "Simcard", "Fuelcard", "Insurance card"];
const MEDIA_FIELD_KEYS = {
  passportPhoto: "passportPhotoBase64",
  ghanaCardFront: "ghanaCardFrontBase64",
  ghanaCardBack: "ghanaCardBackBase64"
};
const DOCUMENT_FIELD_KEYS = new Set(["apparisal", "performanceContract", "certificates", "cv"]);
const MULTI_DOCUMENT_FIELD_KEYS = new Set(["apparisal", "performanceContract", "certificates"]);

// Master field registry for the profile form renderer.
const FIELD_DEFS = {
  employeeNo: { label: "Employee No", type: "text" },
  title: { label: "Title", type: "select", options: ["Mr.", "Ms.", "Mrs."] },
  fullName: { label: "Full Name", type: "text" },
  firstName: { label: "First Name", type: "text" },
  middleName: { label: "Middle Name", type: "text" },
  lastName: { label: "Last Name", type: "text" },
  gender: { label: "Gender", type: "select", options: ["Male", "Female"] },
  dateOfBirth: { label: "Date of Birth (employee birth date)", type: "date" },
  age: { label: "Age", type: "number" },
  maritalStatus: { label: "Marital Status", type: "select", options: ["Single", "Married"] },
  jobTitle: { label: "Job Title", type: "text" },
  category: { label: "Category", type: "select", options: ["Fixed-term", "Permanent"] },
  payrollStatus: { label: "Payroll Status", type: "select", options: ["On payroll", "Table top"] },
  amount: { label: "Salary Amount", type: "currency" },
  grading: { label: "Grading", type: "select", options: ["Managing Director", "Manager", "Senior Officer", "Officer", "Supervisory"] },
  jobGroup: { label: "Job Group", type: "select", options: ["Finance", "Sales & Marketing", "Sales", "HR & Admin", "Administration", "Operations", "Station Manager", "Pump Attendants", "Security"] },
  location: {
    label: "Location",
    type: "select",
    options: [
      "Head Office",
      "Tema",
      "Takoradi",
      "Kumasi",
      "Konongo",
      "Obuasi",
      "Ofoase",
      "Akyease",
      "Prestea",
      "Kutukrom",
      "Ada Foah",
      "McCarthy",
      "Medie",
      "Mankrong Nkwanta",
      "Nsawam",
      "Wassa Juabo",
      "Agogo",
      "Ablekuma",
      "Agona Amenfi",
      "Tikobo No1",
      "Ahobre",
      "Kasoa Liberia",
      "Koforidua",
      "Abokobi",
      "Half Assini"
    ]
  },
  apparisal: { label: "Appraisal", type: "text" },
  performanceContract: { label: "Performance Contract", type: "text" },
  certificates: { label: "Certificates", type: "textarea" },
  cv: { label: "CV", type: "textarea" },
  employmentDate: { label: "Employment Date (date joined)", type: "date" },
  entryChecklist: { label: "Entry Checklist", type: "text" },
  status: { label: "Employment Status", type: "select", options: ["Engaged", "Resigned", "Terminated", "Dismissed", "End of Contract"] },
  exitDate: { label: "Exit Date (last working day)", type: "date" },
  lengthService: { label: "Length of Service", type: "text" },
  exitChecklist: { label: "Exit Checklist", type: "textarea" },
  annualLeaveEntitlement: { label: "Annual Leave Entitlement", type: "number" },
  leaveTaken: { label: "Leave Taken", type: "number" },
  leaveBalance: { label: "Leave Balance", type: "number" },
  socialSecurityNo: { label: "Social Security No", type: "text" },
  bankName: { label: "Bank Name", type: "text" },
  bankBranchName: { label: "Bank Branch Name", type: "text" },
  accountNo: { label: "Account No", type: "text" },
  taxIdentificationNo: { label: "Tax Identification No", type: "text" },
  ghanaCardNumber: { label: "Ghana Card Number", type: "text" },
  providentFund: { label: "Provident Fund", type: "select", options: ["Yes", "No"] },
  vehicleAndFuel: { label: "Vehicle & Fuel", type: "text" },
  transportAllowanceGallons: { label: "Transport Allowance (Gallons)", type: "number" },
  cellPhone: { label: "Cell Phone", type: "select", options: ["Yes", "No"] },
  airtimeAmount: { label: "Airtime Amount (GH₵)", type: "currency" },
  lunch: { label: "Lunch", type: "select", options: ["Yes", "No"] },
  medicalPlan: { label: "Medical", type: "select", options: ["Standard", "Enhanced", "Enhanced plus", "Ultimate"] },
  spouseOrNa: { label: "Spouse / N/A", type: "text" },
  childrenCount: { label: "Number of Children", type: "number" },
  mobilePhoneNo: { label: "Mobile Phone No", type: "text" },
  businessPhoneNo: { label: "Business Phone No", type: "text" },
  homePhoneNo: { label: "Home Phone No", type: "text" },
  emailAddress: { label: "E-mail Address", type: "text" },
  postalAddress: { label: "Postal Address", type: "text" },
  residentialAddress: { label: "Residential Address (Landmarks)", type: "textarea" },
  emergencyContact: { label: "Emergency Contact", type: "text" },
  emergencyPhoneNo: { label: "Emergency Phone No", type: "text" },
  relationship: { label: "Relationship", type: "text" },
  spouseName: { label: "Spouse Name", type: "text" },
  namesOfChildren: { label: "Names of Children", type: "textarea" },
  numberOfDependents: { label: "Number of Dependents", type: "number" },
  nextOfKinName: { label: "Next of Kin (Name)", type: "text" },
  nextOfKinContactDetails: { label: "Contact Details of Next of Kin", type: "textarea" },
  guarantorName: { label: "Guarantor Name", type: "text" },
  guarantorContact: { label: "Guarantor Contact", type: "text" },
  guarantorResidentialAddress: { label: "Guarantor Residential Address", type: "textarea" },
  guarantorGhanaCard: { label: "Ghana Card (Guarantor)", type: "text" }
};
const DEFAULT_LOCATION_OPTIONS = FIELD_DEFS.location.options;
const PROFILE_SECTION_DEFS = [
  {
    key: "personal-info",
    title: "Personal Info",
    fields: [
      "employeeNo", "title", "fullName", "firstName", "middleName", "lastName",
      "gender", "dateOfBirth", "age", "maritalStatus", "ghanaCardNumber"
    ]
  },
  {
    key: "work-info",
    title: "Work Info",
    fields: [
      "jobTitle", "category", "payrollStatus", "grading", "jobGroup", "location",
      "apparisal", "performanceContract", "certificates", "cv",
      "employmentDate", "entryChecklist", "status", "exitDate", "lengthService", "exitChecklist"
    ]
  },
  {
    key: "benefits",
    title: "Benefits",
    fields: [
      "amount", "annualLeaveEntitlement", "leaveTaken", "leaveBalance",
      "providentFund", "vehicleAndFuel", "transportAllowanceGallons", "cellPhone",
      "airtimeAmount", "lunch", "medicalPlan", "spouseOrNa", "childrenCount"
    ]
  },
  {
    key: "bank-and-statutory",
    title: "Bank and Statutory Info",
    fields: [
      "socialSecurityNo", "bankName", "bankBranchName", "accountNo",
      "taxIdentificationNo"
    ]
  },
  {
    key: "contact-and-family",
    title: "Contact and Family",
    fields: [
      "mobilePhoneNo", "businessPhoneNo", "homePhoneNo", "emailAddress",
      "postalAddress", "residentialAddress", "emergencyContact", "emergencyPhoneNo",
      "relationship", "spouseName", "namesOfChildren", "numberOfDependents"
    ]
  },
  {
    key: "next-of-kin-and-guarantor",
    title: "Next of Kin and Guarantor",
    fields: [
      "nextOfKinName", "nextOfKinContactDetails", "guarantorName",
      "guarantorContact", "guarantorResidentialAddress", "guarantorGhanaCard"
    ]
  }
];

const BASE_ORDER = Object.keys(FIELD_DEFS);
// Build a blank default form from field definitions.
const defaultForm = Object.fromEntries(BASE_ORDER.map((k) => [k, ""]));
defaultForm.title = "Mr.";
defaultForm.gender = "Male";
defaultForm.maritalStatus = "Single";
defaultForm.category = "Permanent";
defaultForm.payrollStatus = "On payroll";
defaultForm.grading = "Officer";
defaultForm.jobGroup = "Operations";
defaultForm.location = "Head Office";
defaultForm.status = "Engaged";
defaultForm.providentFund = "Yes";
defaultForm.cellPhone = "No";
defaultForm.lunch = "No";
defaultForm.medicalPlan = "Standard";
defaultForm.spouseOrNa = "N/A";

const parseCustomJson = (raw) => {
  if (!raw) return {};
  try {
    return typeof raw === "string" ? JSON.parse(raw) : raw;
  } catch {
    return {};
  }
};

// Safe localStorage reader that falls back if value is missing/corrupt.
const readStored = (key, fallback) => {
  try {
    const val = localStorage.getItem(key);
    return val ? JSON.parse(val) : fallback;
  } catch {
    return fallback;
  }
};

// Normalize admin-defined custom fields before rendering.
const normalizeCustomDefs = (defs) =>
  (defs || []).map((def) => ({
    key: def.key,
    label: def.label,
    type: def.type || "text",
    options: def.type === "select" ? (def.options || []) : []
  }));

const getInitialTheme = () => {
  const storedTheme = readStored(THEME_KEY, null);
  if (storedTheme === "dark" || storedTheme === "light") return storedTheme;
  if (typeof window !== "undefined" && window.matchMedia) {
    return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
  }
  return "light";
};

const calculateAgeFromDob = (dob) => {
  if (!dob) return "";
  const birthDate = new Date(dob);
  if (Number.isNaN(birthDate.getTime())) return "";
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
    age -= 1;
  }
  return age < 0 ? "" : String(age);
};

const parseChecklistValue = (value) =>
  (value || "")
    .split(",")
    .map((item) => item.trim())
    .filter(Boolean);

const toChecklistValue = (items) => items.join(", ");
const fileToDataUrl = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(String(reader.result || ""));
    reader.onerror = () => reject(new Error("Failed to read selected file."));
    reader.readAsDataURL(file);
  });
const parseDocumentValue = (value) => {
  if (!value) return null;
  if (typeof value !== "string") return null;
  try {
    const parsed = JSON.parse(value);
    if (parsed && typeof parsed === "object" && parsed.dataUrl) {
      return {
        name: parsed.name || "Attached document",
        dataUrl: String(parsed.dataUrl)
      };
    }
  } catch {
    // Fallback for older records that may have raw data URLs.
  }
  if (value.startsWith("data:")) {
    return { name: "Attached document", dataUrl: value };
  }
  return null;
};
const parseDocumentList = (value) => {
  if (!value || typeof value !== "string") return [];
  try {
    const parsed = JSON.parse(value);
    if (Array.isArray(parsed)) {
      return parsed
        .filter((item) => item && typeof item === "object" && item.dataUrl)
        .map((item) => ({
          name: item.name || "Attached document",
          dataUrl: String(item.dataUrl)
        }));
    }
    if (parsed && typeof parsed === "object" && parsed.dataUrl) {
      return [{ name: parsed.name || "Attached document", dataUrl: String(parsed.dataUrl) }];
    }
  } catch {
    // Fallback for raw data URL values.
  }
  if (value.startsWith("data:")) {
    return [{ name: "Attached document", dataUrl: value }];
  }
  return [];
};
const normalizeCurrencyInput = (rawValue) => {
  const cleaned = String(rawValue ?? "").replace(/[^0-9.]/g, "");
  if (!cleaned) return "";
  const firstDotIndex = cleaned.indexOf(".");
  if (firstDotIndex === -1) return cleaned;
  const integerPart = cleaned.slice(0, firstDotIndex);
  const decimalPart = cleaned.slice(firstDotIndex + 1).replace(/\./g, "").slice(0, 2);
  return `${integerPart}.${decimalPart}`;
};

const formatCurrencyInput = (value) => {
  if (value === "") return "";
  const normalized = normalizeCurrencyInput(value);
  if (!normalized) return "";
  const hasDot = normalized.includes(".");
  const [integerPartRaw, decimalPart = ""] = normalized.split(".");
  const integerPart = integerPartRaw || "0";
  const formattedInteger = Number(integerPart).toLocaleString("en-GH");
  if (hasDot) return `GH₵ ${formattedInteger}.${decimalPart}`;
  return `GH₵ ${formattedInteger}`;
};
const toCsvCell = (value) => {
  const normalized = value == null ? "" : String(value);
  const escaped = normalized.replace(/"/g, "\"\"");
  return `"${escaped}"`;
};
const KEYBOARD_NAV_SELECTOR = "input, select, textarea, button, a[href]";

const getFocusableElements = () =>
  Array.from(document.querySelectorAll(KEYBOARD_NAV_SELECTOR))
    .filter((element) => {
      const htmlElement = /** @type {HTMLElement} */ (element);
      return !htmlElement.hasAttribute("disabled") && htmlElement.tabIndex !== -1;
    });

const moveFocusByOffset = (offset) => {
  const focusable = getFocusableElements();
  const activeIndex = focusable.indexOf(document.activeElement);
  if (activeIndex < 0) return;
  const nextIndex = activeIndex + offset;
  if (nextIndex < 0 || nextIndex >= focusable.length) return;
  const nextElement = /** @type {HTMLElement} */ (focusable[nextIndex]);
  nextElement.focus();
};

export default function App() {
  const currentYear = new Date().getFullYear();
  const savedAuth = readStored(AUTH_SESSION_KEY, {});
  // Core app state.
  const [employees, setEmployees] = useState([]);
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState("");
  const [username, setUsername] = useState(savedAuth.username || "administration");
  const [password, setPassword] = useState(savedAuth.password || "HRMI056");
  const [isAuthenticated, setIsAuthenticated] = useState(Boolean(savedAuth.isAuthenticated));
  const [form, setForm] = useState({ ...defaultForm, customFields: {} });
  const [customDefs, setCustomDefs] = useState(() => normalizeCustomDefs(readStored(CUSTOM_KEY, [])));
  const [fieldOrder, setFieldOrder] = useState(() => readStored(ORDER_KEY, BASE_ORDER));
  const [newFieldLabel, setNewFieldLabel] = useState("");
  const [newFieldType, setNewFieldType] = useState("text");
  const [newFieldOptions, setNewFieldOptions] = useState("");
  const [locationOptions, setLocationOptions] = useState(() => {
    const saved = readStored(LOCATION_OPTIONS_KEY, DEFAULT_LOCATION_OPTIONS);
    return Array.isArray(saved) && saved.length > 0 ? saved : DEFAULT_LOCATION_OPTIONS;
  });
  const [newLocation, setNewLocation] = useState("");
  const [currentPage, setCurrentPage] = useState("dashboard");
  const [theme, setTheme] = useState(getInitialTheme);
  const [employeeListFilter, setEmployeeListFilter] = useState("all");
  const [employeeSearch, setEmployeeSearch] = useState("");
  const [listSearch, setListSearch] = useState("");
  const [listPage, setListPage] = useState(1);
  const [listPageSize, setListPageSize] = useState(10);
  const [selectedEmployeeNo, setSelectedEmployeeNo] = useState("");
  const [selectedListEmployeeNo, setSelectedListEmployeeNo] = useState("");
  const [editEmployeeNo, setEditEmployeeNo] = useState("");
  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
  const [confirmDialogMessage, setConfirmDialogMessage] = useState("");
  const confirmResolverRef = useRef(null);
  const usernameInputRef = useRef(null);
  const passwordInputRef = useRef(null);
  const isAdmin = username.trim().toLowerCase() === "administration";
  const requestConfirmation = (message) => new Promise((resolve) => {
    confirmResolverRef.current = resolve;
    setConfirmDialogMessage(message);
    setConfirmDialogOpen(true);
  });
  const closeConfirmation = (accepted) => {
    setConfirmDialogOpen(false);
    if (confirmResolverRef.current) {
      confirmResolverRef.current(accepted);
      confirmResolverRef.current = null;
    }
  };
  const handleKeyboardNavigation = (event) => {
    const target = event.target;
    if (!(target instanceof HTMLElement)) return;
    const isTextArea = target.tagName === "TEXTAREA";
    const isSelect = target.tagName === "SELECT";
    const isActionElement = target.tagName === "BUTTON" || target.tagName === "A";

    if (event.key === "Enter" && !event.shiftKey && !isTextArea) {
      // Let Enter activate actionable controls ("OK"/click behavior).
      if (isActionElement) return;
      event.preventDefault();
      moveFocusByOffset(1);
      return;
    }
    if (isSelect) return;
    if (event.key === "ArrowRight" || event.key === "ArrowDown") {
      event.preventDefault();
      moveFocusByOffset(1);
      return;
    }
    if (event.key === "ArrowLeft" || event.key === "ArrowUp") {
      event.preventDefault();
      moveFocusByOffset(-1);
    }
  };

  // Persist form builder configuration.
  useEffect(() => localStorage.setItem(CUSTOM_KEY, JSON.stringify(customDefs)), [customDefs]);
  useEffect(() => localStorage.setItem(ORDER_KEY, JSON.stringify(fieldOrder)), [fieldOrder]);
  useEffect(() => {
    localStorage.setItem(AUTH_SESSION_KEY, JSON.stringify({
      isAuthenticated,
      username,
      password
    }));
  }, [isAuthenticated, username, password]);
  useEffect(() => localStorage.setItem(LOCATION_OPTIONS_KEY, JSON.stringify(locationOptions)), [locationOptions]);
  useEffect(() => {
    const safeTheme = theme === "dark" ? "dark" : "light";
    document.documentElement.setAttribute("data-theme", safeTheme);
    localStorage.setItem(THEME_KEY, JSON.stringify(safeTheme));
  }, [theme]);

  // Build HTTP basic auth header from current credentials.
  const authHeader = () => ({ Authorization: `Basic ${btoa(`${username}:${password}`)}` });

  // Convert UI values into backend payload types.
  const toPayload = (value) => ({
    ...value,
    age: value.age === "" ? null : Number(value.age),
    amount: value.amount === "" ? null : Number(value.amount),
    annualLeaveEntitlement: value.annualLeaveEntitlement === "" ? null : Number(value.annualLeaveEntitlement),
    leaveTaken: value.leaveTaken === "" ? null : Number(value.leaveTaken),
    leaveBalance: value.leaveBalance === "" ? null : Number(value.leaveBalance),
    transportAllowanceGallons: value.transportAllowanceGallons === "" ? null : Number(value.transportAllowanceGallons),
    airtimeAmount: value.airtimeAmount === "" ? null : Number(value.airtimeAmount),
    childrenCount: value.childrenCount === "" ? null : Number(value.childrenCount),
    numberOfDependents: value.numberOfDependents === "" ? null : Number(value.numberOfDependents),
    providentFund: value.providentFund === "Yes",
    cellPhone: value.cellPhone === "Yes",
    lunch: value.lunch === "Yes",
    customFields: value.customFields || {}
  });

  const normalizedOrder = [
    ...fieldOrder.filter((key) => BASE_ORDER.includes(key) || customDefs.some((d) => `custom:${d.key}` === key)),
    ...BASE_ORDER.filter((key) => !fieldOrder.includes(key)),
    ...customDefs.map((d) => `custom:${d.key}`).filter((key) => !fieldOrder.includes(key))
  ];
  const normalizedBaseOrder = normalizedOrder.filter((key) => !key.startsWith("custom:"));
  const customOrderKeys = normalizedOrder.filter((key) => key.startsWith("custom:"));
  const sectionedBaseFields = PROFILE_SECTION_DEFS.map((section) => ({
    ...section,
    fields: section.fields.filter((fieldKey) => normalizedBaseOrder.includes(fieldKey))
  })).filter((section) => section.fields.length > 0);
  const uncategorizedFields = normalizedBaseOrder.filter(
    (fieldKey) => !PROFILE_SECTION_DEFS.some((section) => section.fields.includes(fieldKey))
  );
  const filteredEmployees = employees.filter((emp) => {
    const q = employeeSearch.trim().toLowerCase();
    if (!q) return false;
    return (emp.employeeNo || "").toLowerCase().includes(q) || (emp.fullName || "").toLowerCase().includes(q);
  }).slice(0, 8);
  const selectedEmployee = employees.find((emp) => emp.employeeNo === selectedEmployeeNo);
  const selectedListEmployee = employees.find((emp) => emp.employeeNo === selectedListEmployeeNo);
  const selectedPassportImage = selectedListEmployee
    ? (
      selectedListEmployee.customFields?.[MEDIA_FIELD_KEYS.passportPhoto]
      || selectedListEmployee.passportPhotoBase64
      || selectedListEmployee.passportPhoto
      || ""
    )
    : "";
  const employeeFilterOptions = [
    { value: "all", label: "All Employees" },
    { value: "married", label: "Married Employees" },
    { value: "single", label: "Single Employees" },
    { value: "engaged", label: "Engaged Employees" },
    { value: "resigned", label: "Resigned Employees" },
    { value: "terminated", label: "Terminated Employees" },
    { value: "dismissed", label: "Dismissed Employees" },
    { value: "end_of_contract", label: "End of Contract Employees" },
    { value: "on_payroll", label: "On Payroll Employees" },
    { value: "table_top", label: "Table Top Employees" }
  ];
  const selectedEmployeeFilter = employeeFilterOptions.find((opt) => opt.value === employeeListFilter) || employeeFilterOptions[0];
  const adminVisibleEmployees = employees.filter((emp) => {
    if (employeeListFilter === "all") return true;
    if (employeeListFilter === "married") return (emp.maritalStatus || "").toLowerCase() === "married";
    if (employeeListFilter === "single") return (emp.maritalStatus || "").toLowerCase() === "single";
    if (employeeListFilter === "engaged") return (emp.status || "").toLowerCase() === "engaged";
    if (employeeListFilter === "resigned") return (emp.status || "").toLowerCase() === "resigned";
    if (employeeListFilter === "terminated") return (emp.status || "").toLowerCase() === "terminated";
    if (employeeListFilter === "dismissed") return (emp.status || "").toLowerCase() === "dismissed";
    if (employeeListFilter === "end_of_contract") return (emp.status || "").toLowerCase() === "end of contract";
    if (employeeListFilter === "on_payroll") return (emp.payrollStatus || "").toLowerCase() === "on payroll";
    if (employeeListFilter === "table_top") return (emp.payrollStatus || "").toLowerCase() === "table top";
    return true;
  });
  const normalizedListQuery = listSearch.trim().toLowerCase();
  const searchedEmployeeList = adminVisibleEmployees.filter((emp) => {
    if (!normalizedListQuery) return true;
    return [
      emp.employeeNo,
      emp.fullName,
      emp.jobTitle,
      emp.jobGroup,
      emp.location,
      emp.status,
      emp.payrollStatus,
      emp.emailAddress
    ].some((value) => (value || "").toLowerCase().includes(normalizedListQuery));
  });
  const totalListPages = Math.max(1, Math.ceil(searchedEmployeeList.length / listPageSize));
  const safeListPage = Math.min(listPage, totalListPages);
  const paginatedEmployeeList = searchedEmployeeList.slice((safeListPage - 1) * listPageSize, safeListPage * listPageSize);

  const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));
  const fetchWithRetry = async (url, options = {}, retries = 3, delayMs = 1200) => {
    let lastError;
    for (let attempt = 1; attempt <= retries; attempt += 1) {
      try {
        return await fetch(url, options);
      } catch (err) {
        lastError = err;
        if (attempt < retries) await sleep(delayMs);
      }
    }
    throw lastError;
  };

  // Fetch HR records and analytics summary in parallel.
  const loadData = async () => {
    try {
      const [employeeRes, summaryRes] = await Promise.all([
        fetchWithRetry(JAVA_API, { headers: authHeader() }),
        fetchWithRetry(PYTHON_API)
      ]);
      if (!employeeRes.ok || !summaryRes.ok) throw new Error("Unable to load admin data. Check credentials and running services.");
      const employeeData = await employeeRes.json();
      const summaryData = await summaryRes.json();
      setEmployees(employeeData.map((emp) => ({ ...emp, customFields: parseCustomJson(emp.customFieldsJson) })));
      setSummary(summaryData);
      setError("");
    } catch (err) {
      setError(err.message || "Failed to fetch. Please try again.");
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      loadData();
    }
  }, [isAuthenticated]);
  useEffect(() => { setListPage(1); }, [employeeListFilter, listSearch, listPageSize]);
  // Keep order list aligned when custom fields are added/removed.
  useEffect(() => { if (JSON.stringify(normalizedOrder) !== JSON.stringify(fieldOrder)) setFieldOrder(normalizedOrder); }, [customDefs]);
  // Auto-compute age whenever date of birth changes.
  useEffect(() => {
    setForm((prev) => {
      const computedAge = calculateAgeFromDob(prev.dateOfBirth);
      if (prev.age === computedAge) return prev;
      return { ...prev, age: computedAge };
    });
  }, [form.dateOfBirth]);
  // Auto-compute leave balance from entitlement - taken.
  useEffect(() => {
    setForm((prev) => {
      if (prev.annualLeaveEntitlement === "" || prev.leaveTaken === "") {
        if (prev.leaveBalance === "") return prev;
        return { ...prev, leaveBalance: "" };
      }
      const entitlement = Number(prev.annualLeaveEntitlement);
      const taken = Number(prev.leaveTaken);
      if (Number.isNaN(entitlement) || Number.isNaN(taken)) return prev;
      const computedBalance = String(entitlement - taken);
      if (prev.leaveBalance === computedBalance) return prev;
      return { ...prev, leaveBalance: computedBalance };
    });
  }, [form.annualLeaveEntitlement, form.leaveTaken]);
  // Verify credentials by calling a protected endpoint.
  const handleLogin = async () => {
    setError("");
    try {
      const res = await fetch(JAVA_API, { headers: authHeader() });
      if (!res.ok) {
        throw new Error("Login failed. Please check username/password.");
      }
      setIsAuthenticated(true);
      loadData();
    } catch (err) {
      setError(err.message || "Login failed.");
    }
  };

  // Reset user session and clear dashboard state.
  const handleLogout = async () => {
    const shouldLogout = await requestConfirmation("Are you sure you want to log out?");
    if (!shouldLogout) return;
    setIsAuthenticated(false);
    setEmployees([]);
    setSummary(null);
    setError("");
    setCurrentPage("dashboard");
    setSecondsLeft(SESSION_TIMEOUT_SECONDS);
    setEmployeeListFilter("all");
    setEmployeeSearch("");
    setListSearch("");
    setListPage(1);
    setListPageSize(10);
    setSelectedEmployeeNo("");
    setSelectedListEmployeeNo("");
    setEditEmployeeNo("");
    localStorage.removeItem(AUTH_SESSION_KEY);
  };

  // Create a new employee profile.
  const createEmployee = async () => {
    const shouldCreate = await requestConfirmation("Are you sure you want to create this employee profile?");
    if (!shouldCreate) return;

    const res = await fetch(JAVA_API, { method: "POST", headers: { "Content-Type": "application/json", ...authHeader() }, body: JSON.stringify(toPayload(form)) });
    if (!res.ok) {
      const errorText = await res.text();
      return setError(`Create failed: ${errorText || "please check required fields and Employee No uniqueness."}`);
    }
    setForm({ ...defaultForm, customFields: {} });
    loadData();
  };

  // Update an employee and backfill legacy fields for compatibility.
  const updateEmployee = async (emp) => {
    const shouldUpdate = await requestConfirmation(`Update employee ${emp.employeeNo || ""}?`);
    if (!shouldUpdate) return;
    const legacyName = emp.legacyEmployeeName || emp.fullName || "";
    const nameParts = legacyName.trim().split(" ").filter(Boolean);
    const safeFirstName = emp.firstName || nameParts[0] || "Unknown";
    const safeLastName = emp.lastName || nameParts[nameParts.length - 1] || "Unknown";
    const safeFullName = emp.fullName || legacyName || `${safeFirstName} ${safeLastName}`.trim();

    const res = await fetch(`${JAVA_API}/${emp.employeeNo}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", ...authHeader() },
      body: JSON.stringify(toPayload({
        ...defaultForm,
        ...emp,
        fullName: safeFullName,
        firstName: safeFirstName,
        lastName: safeLastName,
        providentFund: emp.providentFund ? "Yes" : "No",
        cellPhone: emp.cellPhone ? "Yes" : "No",
        lunch: emp.lunch ? "Yes" : "No",
        customFields: emp.customFields || parseCustomJson(emp.customFieldsJson)
      }))
    });
    if (!res.ok) {
      if (res.status === 401 || res.status === 403) {
        return setError("Update failed: this account is not authorized for update.");
      }
      const errorText = await res.text();
      return setError(`Update failed: ${errorText || "invalid data in this record."}`);
    }
    loadData();
  };

  // Delete a profile by employee number.
  const deleteEmployee = async (employeeNo) => {
    const shouldDelete = await requestConfirmation(`Delete employee ${employeeNo}? This cannot be undone.`);
    if (!shouldDelete) return;
    const res = await fetch(`${JAVA_API}/${employeeNo}`, { method: "DELETE", headers: authHeader() });
    if (!res.ok) return setError("Delete failed. Ensure role is ADMIN/MANAGER.");
    loadData();
  };
  const startEditEmployee = (employee) => {
    if (!employee?.employeeNo) return;
    setForm({
      ...defaultForm,
      ...employee,
      employeeNo: employee.employeeNo,
      providentFund: employee.providentFund ? "Yes" : "No",
      cellPhone: employee.cellPhone ? "Yes" : "No",
      lunch: employee.lunch ? "Yes" : "No",
      customFields: employee.customFields || parseCustomJson(employee.customFieldsJson)
    });
    setEditEmployeeNo(employee.employeeNo);
    setCurrentPage("employee-edit");
    setError("");
  };
  const saveEditedEmployee = async () => {
    if (!editEmployeeNo) return;
    const shouldSave = await requestConfirmation("Save all changes to this employee profile?");
    if (!shouldSave) return;
    const res = await fetch(`${JAVA_API}/${editEmployeeNo}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", ...authHeader() },
      body: JSON.stringify(toPayload({ ...form, employeeNo: editEmployeeNo }))
    });
    if (!res.ok) {
      if (res.status === 401 || res.status === 403) {
        return setError("Save failed: this account is not authorized for update.");
      }
      const errorText = await res.text();
      return setError(`Save failed: ${errorText || "invalid data in this record."}`);
    }
    setError("");
    setCurrentPage("dashboard");
    setEmployeeSearch(editEmployeeNo);
    setSelectedEmployeeNo(editEmployeeNo);
    setEditEmployeeNo("");
    loadData();
  };

  // Move a field up/down in the form designer.
  const moveField = (index, dir) => {
    const target = index + dir;
    if (target < 0 || target >= normalizedOrder.length) return;
    const next = [...normalizedOrder];
    [next[index], next[target]] = [next[target], next[index]];
    setFieldOrder(next);
  };

  // Add an admin-defined custom field to the dynamic form.
  const addCustomField = async () => {
    const label = newFieldLabel.trim();
    if (!label) return;
    const key = label.toLowerCase().replace(/[^a-z0-9]+/g, "_").replace(/^_+|_+$/g, "");
    if (!key) return;
    if (customDefs.some((f) => f.key === key)) return setError("Custom field already exists.");
    const options = newFieldType === "select"
      ? newFieldOptions.split(",").map((o) => o.trim()).filter(Boolean)
      : [];
    if (newFieldType === "select" && options.length < 2) {
      return setError("Select custom field needs at least 2 comma-separated options.");
    }
    const shouldAddField = await requestConfirmation(`Add new custom field "${label}"?`);
    if (!shouldAddField) return;
    const def = { key, label, type: newFieldType, options };
    setCustomDefs((prev) => [...prev, def]);
    setFieldOrder((prev) => [...prev, `custom:${key}`]);
    setForm((prev) => ({
      ...prev,
      customFields: {
        ...prev.customFields,
        [key]: newFieldType === "select" ? options[0] : ""
      }
    }));
    setNewFieldLabel("");
    setNewFieldType("text");
    setNewFieldOptions("");
    setError("");
  };
  const addLocationOption = async () => {
    const locationName = newLocation.trim();
    if (!locationName) return;
    const exists = locationOptions.some((option) => option.toLowerCase() === locationName.toLowerCase());
    if (exists) {
      setError("Location already exists.");
      return;
    }
    const shouldAddLocation = await requestConfirmation(`Add location station "${locationName}"?`);
    if (!shouldAddLocation) return;
    setLocationOptions((prev) => [...prev, locationName]);
    setNewLocation("");
    setError("");
  };
  const removeLocationOption = async (locationName) => {
    if (locationOptions.length <= 1) {
      setError("At least one location must remain.");
      return;
    }
    const shouldRemoveLocation = await requestConfirmation(`Remove location station "${locationName}"?`);
    if (!shouldRemoveLocation) return;
    setLocationOptions((prev) => prev.filter((option) => option !== locationName));
    if (form.location === locationName) {
      const fallback = locationOptions.find((option) => option !== locationName) || "";
      setValue("location", fallback);
    }
    setError("");
  };

  // Generic field setters for base fields and custom fields.
  const setValue = (key, value) => setForm((prev) => ({ ...prev, [key]: value }));
  const setCustomValue = (key, value) => setForm((prev) => ({ ...prev, customFields: { ...prev.customFields, [key]: value } }));
  const toggleChecklistValue = (key, option) => {
    setForm((prev) => {
      const selected = parseChecklistValue(prev[key]);
      const nextSelected = selected.includes(option)
        ? selected.filter((item) => item !== option)
        : [...selected, option];
      return { ...prev, [key]: toChecklistValue(nextSelected) };
    });
  };
  const handleMediaUpload = async (mediaKey, file) => {
    if (!file) return;
    if (!file.type.startsWith("image/")) {
      setError("Please upload only image files for employee media.");
      return;
    }
    try {
      const dataUrl = await fileToDataUrl(file);
      setForm((prev) => ({
        ...prev,
        customFields: {
          ...prev.customFields,
          [mediaKey]: dataUrl
        }
      }));
      setError("");
    } catch (err) {
      setError(err.message || "Unable to load selected image.");
    }
  };
  const handleDocumentUpload = async (fieldKey, file) => {
    if (!file) return;
    try {
      const dataUrl = await fileToDataUrl(file);
      if (MULTI_DOCUMENT_FIELD_KEYS.has(fieldKey)) {
        const existingDocs = parseDocumentList(form[fieldKey]);
        const nextDocs = [...existingDocs, { name: file.name, dataUrl }];
        setValue(fieldKey, JSON.stringify(nextDocs));
      } else {
        setValue(fieldKey, JSON.stringify({
          name: file.name,
          dataUrl
        }));
      }
      setError("");
    } catch (err) {
      setError(err.message || "Unable to load selected document.");
    }
  };
  const clearDocumentUpload = (fieldKey) => setValue(fieldKey, "");
  const removeDocumentAt = (fieldKey, index) => {
    const existingDocs = parseDocumentList(form[fieldKey]);
    const nextDocs = existingDocs.filter((_, docIndex) => docIndex !== index);
    setValue(fieldKey, nextDocs.length ? JSON.stringify(nextDocs) : "");
  };
  const clearMediaUpload = (mediaKey) => {
    setForm((prev) => ({
      ...prev,
      customFields: {
        ...prev.customFields,
        [mediaKey]: ""
      }
    }));
  };
  const exportEmployeeDetailPdf = async () => {
    window.print();
  };
  const exportEmployeesCsv = () => {
    const customFieldKeys = Array.from(
      new Set(employees.flatMap((emp) => Object.keys(emp.customFields || {})))
    ).sort((a, b) => a.localeCompare(b));
    const columns = [...BASE_ORDER, ...customFieldKeys.map((key) => `custom:${key}`)];
    const header = columns.map((key) => (
      key.startsWith("custom:")
        ? key.replace("custom:", "Custom - ")
        : FIELD_DEFS[key]?.label || key
    ));
    const rows = employees.map((emp) => columns.map((key) => {
      if (key.startsWith("custom:")) {
        const customKey = key.replace("custom:", "");
        return toCsvCell(emp.customFields?.[customKey] ?? "");
      }
      const value = emp[key];
      if (typeof value === "boolean") return toCsvCell(value ? "Yes" : "No");
      return toCsvCell(value ?? "");
    }));

    const csv = [header.map(toCsvCell).join(","), ...rows.map((row) => row.join(","))].join("\n");
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `employees-${new Date().toISOString().slice(0, 10)}.csv`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  // Dynamic field renderer used by the profile page.
  const renderField = (key) => {
    if (key.startsWith("custom:")) {
      const customKey = key.replace("custom:", "");
      const def = customDefs.find((d) => d.key === customKey);
      const label = def?.label || customKey;
      const value = form.customFields?.[customKey] || "";
      if (def?.type === "select") {
        const options = def.options || [];
        const selectValue = value || options[0] || "";
        return (
          <div key={key} className="field-block">
            <label className="field-label">{label}</label>
            <select value={selectValue} onChange={(e) => setCustomValue(customKey, e.target.value)}>
              {options.map((opt) => <option key={opt}>{opt}</option>)}
            </select>
          </div>
        );
      }
      return (
        <div key={key} className="field-block">
          <label className="field-label">{label}</label>
          <input type={def?.type || "text"} value={value} onChange={(e) => setCustomValue(customKey, e.target.value)} />
        </div>
      );
    }
    const def = FIELD_DEFS[key];
    if (!def) return null;
    const fieldClassName = `field-block field-${key}`;
    if (DOCUMENT_FIELD_KEYS.has(key)) {
      const isMultiDocument = MULTI_DOCUMENT_FIELD_KEYS.has(key);
      const documentValue = parseDocumentValue(form[key]);
      const documentList = parseDocumentList(form[key]);
      return (
        <div key={key} className={fieldClassName}>
          <label className="field-label">{def.label}</label>
          <input
            type="file"
            onChange={(e) => handleDocumentUpload(key, e.target.files?.[0])}
          />
          {isMultiDocument ? (
            documentList.length > 0 && (
              <div className="media-preview-card">
                {documentList.map((doc, index) => (
                  <div key={`${key}-${index}`} className="detail-item">
                    <span>{doc.name}</span>
                    <div className="row">
                      <a className="btn btn-secondary" href={doc.dataUrl} target="_blank" rel="noreferrer">
                        Open
                      </a>
                      <a className="btn btn-secondary" href={doc.dataUrl} download={doc.name}>
                        Download
                      </a>
                      <button className="btn btn-secondary" type="button" onClick={() => removeDocumentAt(key, index)}>
                        Remove
                      </button>
                    </div>
                  </div>
                ))}
                <button className="btn btn-secondary" type="button" onClick={() => clearDocumentUpload(key)}>
                  Remove All
                </button>
              </div>
            )
          ) : (
            documentValue && (
              <div className="media-preview-card">
                <span>{documentValue.name}</span>
                <div className="row">
                  <a className="btn btn-secondary" href={documentValue.dataUrl} target="_blank" rel="noreferrer">
                    Open
                  </a>
                  <a className="btn btn-secondary" href={documentValue.dataUrl} download={documentValue.name}>
                    Download
                  </a>
                  <button className="btn btn-secondary" type="button" onClick={() => clearDocumentUpload(key)}>
                    Remove
                  </button>
                </div>
              </div>
            )
          )}
        </div>
      );
    }
    if (key === "entryChecklist" || key === "exitChecklist") {
      const selected = parseChecklistValue(form[key]);
      return (
        <div key={key} className="field-block">
          <label className="field-label">{def.label}</label>
          <div className="checklist-group">
            {CHECKLIST_OPTIONS.map((option) => (
              <label key={`${key}-${option}`} className="checklist-item">
                <input
                  type="checkbox"
                  checked={selected.includes(option)}
                  onChange={() => toggleChecklistValue(key, option)}
                />
                <span>{option}</span>
              </label>
            ))}
          </div>
        </div>
      );
    }
    if (def.type === "select") {
      const selectOptions = key === "location" ? locationOptions : def.options;
      return (
        <div key={key} className={fieldClassName}>
          <label className="field-label">{def.label}</label>
          <select value={form[key]} onChange={(e) => setValue(key, e.target.value)}>{selectOptions.map((opt) => <option key={opt}>{opt}</option>)}</select>
          {def.hint && <p className="field-hint">{def.hint}</p>}
        </div>
      );
    }
    if (def.type === "textarea") {
      return (
        <div key={key} className={fieldClassName}>
          <label className="field-label">{def.label}</label>
          <textarea value={form[key]} onChange={(e) => setValue(key, e.target.value)} />
          {def.hint && <p className="field-hint">{def.hint}</p>}
        </div>
      );
    }
    if (def.type === "currency") {
      const normalizedCurrencyValue = normalizeCurrencyInput(form[key]);
      return (
        <div key={key} className={fieldClassName}>
          <label className="field-label">{def.label}</label>
          <input
            type="text"
            inputMode="decimal"
            placeholder="GH₵ 0.00"
            value={formatCurrencyInput(normalizedCurrencyValue)}
            onChange={(e) => setValue(key, normalizeCurrencyInput(e.target.value))}
          />
          {def.hint && <p className="field-hint">{def.hint}</p>}
        </div>
      );
    }
    return (
      <div key={key} className={fieldClassName}>
        <label className="field-label">{def.label}</label>
        <input
          type={def.type}
          value={form[key]}
          onChange={(e) => setValue(key, e.target.value)}
          max={key === "dateOfBirth" ? "2010-12-31" : undefined}
          readOnly={key === "age" || key === "leaveBalance"}
        />
        {def.hint && <p className="field-hint">{def.hint}</p>}
      </div>
    );
  };

  if (!isAuthenticated) {
    // Login page.
    return (
      <main className="login-shell" onKeyDownCapture={handleKeyboardNavigation}>
        <section className="login-card">
          <p className="eyebrow"><span className="telenergy">TELENERGY</span> staff database</p>
          <h1>HR Login</h1>
          <p className="hero-subtitle">Sign in first to access your employee's records.</p>
          {error && <p className="error">{error}</p>}
          <div className="login-grid">
            <input
              ref={usernameInputRef}
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  passwordInputRef.current?.focus();
                }
              }}
              placeholder="Username"
            />
            <input
              ref={passwordInputRef}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  handleLogin();
                }
              }}
              type="password"
              placeholder="Password"
            />
            <button className="btn btn-primary" onClick={handleLogin}>Sign In</button>
          </div>
        </section>
      </main>
    );
  }

  return (
    // Authenticated application shell.
    <main className="container" onKeyDownCapture={handleKeyboardNavigation}>
      <header className="hero">
        <p className="eyebrow"><span className="telenergy">TELENERGY</span> staff database</p>
        <h1>
          {currentPage === "dashboard" && "HR Database Dashboard"}
          {currentPage === "employee-list" && "Employee List View"}
          {currentPage === "employee-detail" && "Employee Detail View"}
          {currentPage === "employee-edit" && "Edit Employee Details"}
          {currentPage === "designer" && "Admin Form Designer"}
          {currentPage === "profile" && "Create Employee Profile"}
        </h1>
        <p className="hero-subtitle">
          {currentPage === "dashboard" && "Manage complete employee HR profiles, employment status, benefits, contact details, and custom fields."}
          {currentPage === "employee-list" && "Spreadsheet view of employees based on the selected dashboard filter."}
          {currentPage === "employee-detail" && "Complete employee profile details in list form."}
          {currentPage === "employee-edit" && "Update employee details and click Save to lock changes in."}
          {currentPage === "designer" && "Re-order fields and add custom employee information fields."}
          {currentPage === "profile" && "Capture a complete employee profile with all required information."}
        </p>
      </header>
      <section className="icard session-card">
        <div className="section-head"><h2>Session</h2><small>Logged in as {username}</small></div>
        <div className="row session-actions">
          <button className="btn btn-secondary" onClick={() => setCurrentPage("dashboard")}>Dashboard</button>
          <button className="btn btn-primary" onClick={loadData}>Reload Data</button>
          <button className="btn btn-secondary" onClick={() => setTheme((prev) => (prev === "dark" ? "light" : "dark"))}>
            {theme === "dark" ? "Light Mode" : "Dark Mode"}
          </button>
          <button className="btn btn-secondary" onClick={handleLogout}>Log Out</button>
        </div>
      </section>
      {error && <p className="error">{error}</p>}

      {currentPage === "dashboard" && summary && (
        <div className="summary-grid">
          <section className="metric-card">
            <p>Total Employees</p>
            <strong>{summary.totalEmployees ?? 0}</strong>
          </section>
          <section className="metric-card">
            <p>Engaged Employees</p>
            <strong>{summary.activeEmployees ?? 0}</strong>
          </section>
          <section className="metric-card">
            <p>On Payroll</p>
            <strong>{summary.onPayrollEmployees ?? 0}</strong>
          </section>
          {isAdmin && (
            <section className="metric-card view-employee-card">
              <p>View Employee</p>
              <div className="view-employee-select-wrap">
                <select
                  value={employeeListFilter}
                  onChange={(e) => {
                    setEmployeeListFilter(e.target.value);
                    setCurrentPage("employee-list");
                  }}
                  aria-label="View employee filter"
                >
                  {employeeFilterOptions.map((opt) => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                </select>
                <span className="view-employee-caret">▼</span>
              </div>
            </section>
          )}
        </div>
      )}

      {currentPage === "dashboard" && (
        <section id="records" className="card">
          <div className="section-head"><h2>Find Employee</h2><small>Type Employee ID or Full Name</small></div>
          <div className="search-wrap">
            <input
              placeholder="Enter employee ID or name..."
              value={employeeSearch}
              onChange={(e) => setEmployeeSearch(e.target.value)}
            />
            {filteredEmployees.length > 0 && (
              <div className="search-dropdown">
                {filteredEmployees.map((emp) => (
                  <button
                    key={emp.employeeNo}
                    className="search-option"
                    onClick={() => {
                      setSelectedEmployeeNo(emp.employeeNo);
                      setEmployeeSearch(`${emp.employeeNo} - ${emp.fullName || ""}`);
                    }}
                  >
                    <strong>{emp.employeeNo}</strong> - {emp.fullName || "No name"}
                  </button>
                ))}
              </div>
            )}
          </div>

          {selectedEmployee && (
            <div className="selected-employee">
              <p><strong>Employee No:</strong> {selectedEmployee.employeeNo}</p>
              <p><strong>Full Name:</strong> {selectedEmployee.fullName}</p>
              <p><strong>Job Title:</strong> {selectedEmployee.jobTitle}</p>
              <p><strong>Location:</strong> {selectedEmployee.location}</p>
              <div className="row">
                <button className="btn btn-secondary" onClick={() => startEditEmployee(selectedEmployee)}>Edit Details</button>
                <button className="btn btn-danger" onClick={() => deleteEmployee(selectedEmployee.employeeNo)}>Delete</button>
              </div>
            </div>
          )}
        </section>
      )}

      {currentPage === "employee-edit" && isAdmin && editEmployeeNo && (
        <section className="card">
          <div className="section-head">
            <h2>Edit Employee Profile</h2>
            <small>Employee No: {editEmployeeNo}</small>
          </div>
          <div className="profile-sections">
            {sectionedBaseFields.map((section) => (
              <section key={section.key} className="profile-group">
                <h3>{section.title}</h3>
                <div className="grid profile-group-grid">
                  {section.fields.map((key) => renderField(key))}
                </div>
              </section>
            ))}

            {uncategorizedFields.length > 0 && (
              <section className="profile-group">
                <h3>Other Info</h3>
                <div className="grid profile-group-grid">
                  {uncategorizedFields.map((key) => renderField(key))}
                </div>
              </section>
            )}

            {customOrderKeys.length > 0 && (
              <section className="profile-group">
                <h3>Custom Info</h3>
                <div className="grid profile-group-grid">
                  {customOrderKeys.map((key) => renderField(key))}
                </div>
              </section>
            )}

            <section className="profile-group">
              <h3>Identity Images</h3>
              <div className="grid profile-group-grid">
                <div className="field-block media-upload-block">
                  <label className="field-label">Passport Picture</label>
                  <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.passportPhoto, e.target.files?.[0])} />
                  {form.customFields?.[MEDIA_FIELD_KEYS.passportPhoto] && (
                    <div className="media-preview-card">
                      <img src={form.customFields[MEDIA_FIELD_KEYS.passportPhoto]} alt="Passport preview" className="media-preview-image" />
                      <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.passportPhoto)}>Remove</button>
                    </div>
                  )}
                </div>

                <div className="field-block media-upload-block">
                  <label className="field-label">Ghana Card Picture (Front)</label>
                  <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.ghanaCardFront, e.target.files?.[0])} />
                  {form.customFields?.[MEDIA_FIELD_KEYS.ghanaCardFront] && (
                    <div className="media-preview-card">
                      <img src={form.customFields[MEDIA_FIELD_KEYS.ghanaCardFront]} alt="Ghana card front preview" className="media-preview-image" />
                      <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.ghanaCardFront)}>Remove</button>
                    </div>
                  )}
                </div>

                <div className="field-block media-upload-block">
                  <label className="field-label">Ghana Card Picture (Back)</label>
                  <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.ghanaCardBack, e.target.files?.[0])} />
                  {form.customFields?.[MEDIA_FIELD_KEYS.ghanaCardBack] && (
                    <div className="media-preview-card">
                      <img src={form.customFields[MEDIA_FIELD_KEYS.ghanaCardBack]} alt="Ghana card back preview" className="media-preview-image" />
                      <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.ghanaCardBack)}>Remove</button>
                    </div>
                  )}
                </div>
              </div>
            </section>

            <div className="profile-create-actions">
              <button className="btn btn-primary" onClick={saveEditedEmployee}>Save Changes</button>
              <button className="btn btn-secondary" onClick={() => { setCurrentPage("dashboard"); setEditEmployeeNo(""); }}>Cancel</button>
            </div>
          </div>
        </section>
      )}

      {currentPage === "employee-list" && isAdmin && (
        <section className="card spreadsheet-card">
          <div className="section-head">
            <h2>{selectedEmployeeFilter.label}</h2>
            <small>{searchedEmployeeList.length} record(s)</small>
          </div>
          <div className="row sheet-toolbar">
            <button className="btn btn-secondary" onClick={() => setCurrentPage("dashboard")}>Back to Dashboard</button>
            <button className="btn btn-primary" type="button" onClick={exportEmployeesCsv}>Export All (CSV)</button>
            <input
              value={listSearch}
              onChange={(e) => setListSearch(e.target.value)}
              placeholder="Search in this list..."
            />
            <select value={listPageSize} onChange={(e) => setListPageSize(Number(e.target.value))}>
              {[10, 20, 50].map((size) => <option key={size} value={size}>{size} / page</option>)}
            </select>
            <div className="sheet-pagination">
              <button className="btn btn-secondary" disabled={safeListPage <= 1} onClick={() => setListPage((prev) => Math.max(1, prev - 1))}>Prev</button>
              <small>Page {safeListPage} of {totalListPages}</small>
              <button className="btn btn-secondary" disabled={safeListPage >= totalListPages} onClick={() => setListPage((prev) => Math.min(totalListPages, prev + 1))}>Next</button>
            </div>
          </div>
          <div className="sheet-wrap">
            <table className="data-table sheet-table">
              <thead>
                <tr>
                  <th>Employee No</th>
                  <th>Full Name</th>
                  <th>Marital Status</th>
                  <th>Status</th>
                  <th>Payroll Status</th>
                  <th>Job Title</th>
                  <th>Job Group</th>
                  <th>Location</th>
                  <th>Mobile Phone</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                {paginatedEmployeeList.length > 0 ? paginatedEmployeeList.map((emp) => (
                  <tr
                    key={emp.employeeNo}
                    className="employee-row"
                    onClick={() => {
                      setSelectedListEmployeeNo(emp.employeeNo);
                      setCurrentPage("employee-detail");
                    }}
                  >
                    <td>{emp.employeeNo || "-"}</td>
                    <td>{emp.fullName || "-"}</td>
                    <td>{emp.maritalStatus || "-"}</td>
                    <td>{emp.status || "-"}</td>
                    <td>{emp.payrollStatus || "-"}</td>
                    <td>{emp.jobTitle || "-"}</td>
                    <td>{emp.jobGroup || "-"}</td>
                    <td>{emp.location || "-"}</td>
                    <td>{emp.mobilePhoneNo || "-"}</td>
                    <td>{emp.emailAddress || "-"}</td>
                  </tr>
                )) : (
                  <tr>
                    <td colSpan={10}>No employees found for this filter/search.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      )}

      {currentPage === "employee-detail" && isAdmin && selectedListEmployee && (
        <section className="card employee-detail-card">
          <aside className="detail-passport-pin">
            <strong>Passport Picture</strong>
            {selectedPassportImage ? (
              <img
                src={selectedPassportImage}
                alt="Employee passport"
                className="media-preview-image"
              />
            ) : (
              <span>No passport uploaded</span>
            )}
          </aside>
          <div className="section-head">
            <h2>{selectedListEmployee.fullName || selectedListEmployee.employeeNo}</h2>
            <small>Employee No: {selectedListEmployee.employeeNo}</small>
          </div>
          <div className="row no-print">
            <button className="btn btn-secondary" onClick={() => setCurrentPage("employee-list")}>Back to Employee List</button>
            <button className="btn btn-primary" type="button" onClick={exportEmployeeDetailPdf}>Export to PDF</button>
          </div>
          <div className="detail-list detail-list-with-passport">
            {BASE_ORDER.map((fieldKey) => (
              <div key={fieldKey} className="detail-item">
                <strong>{FIELD_DEFS[fieldKey]?.label || fieldKey}</strong>
                {DOCUMENT_FIELD_KEYS.has(fieldKey) ? (
                  (() => {
                    if (MULTI_DOCUMENT_FIELD_KEYS.has(fieldKey)) {
                      const documentList = parseDocumentList(selectedListEmployee[fieldKey]);
                      return documentList.length > 0 ? (
                        <div className="media-preview-card">
                          {documentList.map((doc, index) => (
                            <a key={`${fieldKey}-detail-${index}`} href={doc.dataUrl} target="_blank" rel="noreferrer">
                              {doc.name}
                            </a>
                          ))}
                        </div>
                      ) : (
                        <span>-</span>
                      );
                    }
                    const documentValue = parseDocumentValue(selectedListEmployee[fieldKey]);
                    return documentValue
                      ? <a href={documentValue.dataUrl} target="_blank" rel="noreferrer">{documentValue.name}</a>
                      : <span>-</span>;
                  })()
                ) : (
                  <span>{selectedListEmployee[fieldKey] ?? "-"}</span>
                )}
              </div>
            ))}
          </div>
          {(selectedListEmployee.customFields?.[MEDIA_FIELD_KEYS.ghanaCardFront]
            || selectedListEmployee.customFields?.[MEDIA_FIELD_KEYS.ghanaCardBack]) && (
            <div className="detail-media-section">
              <h3>Employee Images</h3>
              <div className="detail-media-grid">
                {selectedListEmployee.customFields?.[MEDIA_FIELD_KEYS.ghanaCardFront] && (
                  <div className="detail-media-item">
                    <strong>Ghana Card (Front)</strong>
                    <img
                      src={selectedListEmployee.customFields[MEDIA_FIELD_KEYS.ghanaCardFront]}
                      alt="Employee Ghana card front"
                      className="media-preview-image"
                    />
                  </div>
                )}
                {selectedListEmployee.customFields?.[MEDIA_FIELD_KEYS.ghanaCardBack] && (
                  <div className="detail-media-item">
                    <strong>Ghana Card (Back)</strong>
                    <img
                      src={selectedListEmployee.customFields[MEDIA_FIELD_KEYS.ghanaCardBack]}
                      alt="Employee Ghana card back"
                      className="media-preview-image"
                    />
                  </div>
                )}
              </div>
            </div>
          )}
        </section>
      )}

      {isAdmin && currentPage === "designer" && (
        <section id="designer" className="card">
          <div className="section-head"><h2>Admin Form Designer</h2><small>Re-order fields and add new custom employee info fields.</small></div>
          <div className="row">
            <input value={newFieldLabel} onChange={(e) => setNewFieldLabel(e.target.value)} placeholder="New field label (e.g. Passport Number)" />
            <select value={newFieldType} onChange={(e) => setNewFieldType(e.target.value)}>
              <option value="text">Text</option>
              <option value="number">Number</option>
              <option value="date">Date</option>
              <option value="select">Select</option>
            </select>
            {newFieldType === "select" && (
              <input
                value={newFieldOptions}
                onChange={(e) => setNewFieldOptions(e.target.value)}
                placeholder="Options (comma-separated)"
              />
            )}
            <button className="btn btn-primary" onClick={addCustomField}>Add Field</button>
          </div>
          <div className="designer-list">
            {normalizedOrder.map((key, idx) => (
              <div className="designer-item" key={key}>
                <span>
                  {key.startsWith("custom:")
                    ? `${customDefs.find((d) => d.key === key.replace("custom:", ""))?.label || key} (${customDefs.find((d) => d.key === key.replace("custom:", ""))?.type || "text"})`
                    : FIELD_DEFS[key]?.label}
                </span>
                <div className="actions">
                  <button className="btn btn-secondary" onClick={() => moveField(idx, -1)}>Up</button>
                  <button className="btn btn-secondary" onClick={() => moveField(idx, 1)}>Down</button>
                </div>
              </div>
            ))}
          </div>
          <div className="section-head" style={{ marginTop: "16px" }}>
            <h2>Location Stations</h2>
            <small>Add or remove stations for the Location dropdown.</small>
          </div>
          <div className="row">
            <input
              value={newLocation}
              onChange={(e) => setNewLocation(e.target.value)}
              placeholder="Add station (e.g. Airport Junction)"
            />
            <button className="btn btn-primary" type="button" onClick={addLocationOption}>Add Station</button>
          </div>
          <div className="designer-list">
            {locationOptions.map((location) => (
              <div className="designer-item" key={location}>
                <span>{location}</span>
                <div className="actions">
                  <button className="btn btn-danger" type="button" onClick={() => removeLocationOption(location)}>Remove</button>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {currentPage === "profile" && (
      <section id="profile" className="card">
        <div className="section-head"><h2>Create Employee Profile</h2></div>
        <div className="profile-sections">
          {sectionedBaseFields.map((section) => (
            <section key={section.key} className="profile-group">
              <h3>{section.title}</h3>
              <div className="grid profile-group-grid">
                {section.fields.map((key) => renderField(key))}
              </div>
            </section>
          ))}

          {uncategorizedFields.length > 0 && (
            <section className="profile-group">
              <h3>Other Info</h3>
              <div className="grid profile-group-grid">
                {uncategorizedFields.map((key) => renderField(key))}
              </div>
            </section>
          )}

          {customOrderKeys.length > 0 && (
            <section className="profile-group">
              <h3>Custom Info</h3>
              <div className="grid profile-group-grid">
                {customOrderKeys.map((key) => renderField(key))}
              </div>
            </section>
          )}

          <section className="profile-group">
            <h3>Identity Images</h3>
            <div className="grid profile-group-grid">
              <div className="field-block media-upload-block">
                <label className="field-label">Passport Picture</label>
                <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.passportPhoto, e.target.files?.[0])} />
                {form.customFields?.[MEDIA_FIELD_KEYS.passportPhoto] && (
                  <div className="media-preview-card">
                    <img src={form.customFields[MEDIA_FIELD_KEYS.passportPhoto]} alt="Passport preview" className="media-preview-image" />
                    <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.passportPhoto)}>Remove</button>
                  </div>
                )}
              </div>

              <div className="field-block media-upload-block">
                <label className="field-label">Ghana Card Picture (Front)</label>
                <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.ghanaCardFront, e.target.files?.[0])} />
                {form.customFields?.[MEDIA_FIELD_KEYS.ghanaCardFront] && (
                  <div className="media-preview-card">
                    <img src={form.customFields[MEDIA_FIELD_KEYS.ghanaCardFront]} alt="Ghana card front preview" className="media-preview-image" />
                    <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.ghanaCardFront)}>Remove</button>
                  </div>
                )}
              </div>

              <div className="field-block media-upload-block">
                <label className="field-label">Ghana Card Picture (Back)</label>
                <input type="file" accept="image/*" onChange={(e) => handleMediaUpload(MEDIA_FIELD_KEYS.ghanaCardBack, e.target.files?.[0])} />
                {form.customFields?.[MEDIA_FIELD_KEYS.ghanaCardBack] && (
                  <div className="media-preview-card">
                    <img src={form.customFields[MEDIA_FIELD_KEYS.ghanaCardBack]} alt="Ghana card back preview" className="media-preview-image" />
                    <button className="btn btn-secondary" type="button" onClick={() => clearMediaUpload(MEDIA_FIELD_KEYS.ghanaCardBack)}>Remove</button>
                  </div>
                )}
              </div>
            </div>
          </section>

          <div className="profile-create-actions">
            <button className="btn btn-primary" onClick={createEmployee}>Create</button>
          </div>
        </div>
      </section>
      )}

      {currentPage === "dashboard" && (
        <div className="floating-actions">
          {isAdmin && (
            <button className="btn btn-primary" onClick={() => setCurrentPage("designer")}>
              Form Designer
            </button>
          )}
          <button className="btn btn-primary" onClick={() => setCurrentPage("profile")}>
            Employee Profile
          </button>
        </div>
      )}
      {confirmDialogOpen && (
        <div className="confirm-overlay" role="dialog" aria-modal="true" aria-label="Confirmation dialog">
          <div className="confirm-card">
            <h3>Please Confirm</h3>
            <p>{confirmDialogMessage}</p>
            <div className="confirm-actions">
              <button className="btn btn-secondary" type="button" onClick={() => closeConfirmation(false)}>
                Cancel
              </button>
              <button className="btn btn-primary" type="button" onClick={() => closeConfirmation(true)}>
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
      <footer className="app-footer">
        <span>Copyright &copy; {currentYear} TELENERGY. All rights reserved.</span>
        <div className="app-footer-links">
          <a href="/client-guide.html" target="_blank" rel="noreferrer">Client Guide</a>
          <a href="http://localhost:5173" target="_blank" rel="noreferrer">App</a>
        </div>
      </footer>
    </main>
  );
}
