import { useEffect, useState } from 'react';

const API_BASE = 'http://127.0.0.1:8082/api';
const initialForm = {
  id: '',
  title: '',
  description: '',
  customerId: '',
  assignedTechnician: '',
  status: 'Scheduled',
  scheduledDate: '',
  serviceNotes: ''
};

export default function App() {
  const [jobs, setJobs] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    const [jobsRes, customersRes] = await Promise.all([
      fetch(`${API_BASE}/jobs`),
      fetch(`${API_BASE}/customers`)
    ]);
    setJobs(await jobsRes.json());
    setCustomers(await customersRes.json());
    setLoading(false);
  };

  const createJob = async (e) => {
    e.preventDefault();
    const response = await fetch(`${API_BASE}/jobs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    });
    if (response.ok) {
      setMessage('Job created successfully');
      setForm(initialForm);
      fetchData();
    } else {
      const error = await response.text();
      setMessage(`Job request failed: ${error}`);
    }
  };

  return (
    <main className="app-shell">
      <header className="topbar">
        <h1>HVAC Shop Workflow</h1>
        <p>Jobs, customers, and scheduling for local service teams.</p>
      </header>
      <section className="grid-layout">
        <article className="card wide">
          <h2>Active jobs</h2>
          {loading ? (
            <p>Loading jobs...</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Customer</th>
                  <th>Technician</th>
                  <th>Status</th>
                  <th>Schedule</th>
                </tr>
              </thead>
              <tbody>
                {jobs.map((job) => (
                  <tr key={job.id}>
                    <td>{job.id}</td>
                    <td>{job.title}</td>
                    <td>{job.customerId}</td>
                    <td>{job.assignedTechnician}</td>
                    <td>{job.status}</td>
                    <td>{job.scheduledDate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </article>
        <article className="card">
          <h2>Create a service job</h2>
          <form onSubmit={createJob}>
            <label>
              Job ID
              <input value={form.id} onChange={(e) => setForm({ ...form, id: e.target.value })} required />
            </label>
            <label>
              Title
              <input value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
            </label>
            <label>
              Customer
              <select value={form.customerId} onChange={(e) => setForm({ ...form, customerId: e.target.value })} required>
                <option value="">Select one</option>
                {customers.map((customer) => (
                  <option key={customer.id} value={customer.id}> {customer.name} </option>
                ))}
              </select>
            </label>
            <label>
              Technician
              <input value={form.assignedTechnician} onChange={(e) => setForm({ ...form, assignedTechnician: e.target.value })} />
            </label>
            <label>
              Scheduled date
              <input type="date" value={form.scheduledDate} onChange={(e) => setForm({ ...form, scheduledDate: e.target.value })} required />
            </label>
            <label>
              Status
              <select value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}>
                <option>Scheduled</option>
                <option>In Progress</option>
                <option>Completed</option>
                <option>Cancelled</option>
              </select>
            </label>
            <label>
              Notes
              <textarea value={form.serviceNotes} onChange={(e) => setForm({ ...form, serviceNotes: e.target.value })} />
            </label>
            <button type="submit">Create job</button>
          </form>
          {message && <div className="message">{message}</div>}
        </article>
        <article className="card">
          <h2>Customers</h2>
          <ul>
            {customers.map((customer) => (
              <li key={customer.id}>
                <strong>{customer.name}</strong>
                <br />
                {customer.address}
              </li>
            ))}
          </ul>
        </article>
      </section>
    </main>
  );
}
