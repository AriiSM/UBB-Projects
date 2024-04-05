using Microsoft.Data.SqlClient;
using System.Data;
using System.Data.SqlClient;
using System.Security.Cryptography;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace SGBD_Lab1
{
    public partial class Form1 : Form
    {

        private string connectionString = "Server=ARI\\SQLEXPRESS;Database=FirmaDeTransportExtern;Integrated Security=True;TrustServerCertificate=true";
        private DataSet dataSet = new DataSet();
        private SqlDataAdapter parentAdapter;
        private SqlDataAdapter childAdapter;
        private BindingSource bsParent;
        private BindingSource bsChild;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    parentAdapter = new SqlDataAdapter("SELECT * FROM Firma_transport", connection);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Angajati", connection);

                    parentAdapter.Fill(dataSet, "Firma_transport");
                    childAdapter.Fill(dataSet, "Angajati");

                    DataColumn pkColumn = dataSet.Tables["Firma_transport"].Columns["caen"];
                    DataColumn fkColumn = dataSet.Tables["Angajati"].Columns["caen"];

                    DataRelation relation = new DataRelation("FK_Firma_transport_Angajati", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bsParent = new BindingSource();
                    bsChild = new BindingSource();

                    bsParent.DataSource = dataSet.Tables["Firma_transport"];
                    FIrma_DGW.DataSource = bsParent;


                    bsChild.DataMember = "FK_Firma_transport_Angajati";
                    bsChild.DataSource = bsParent;
                    Angajati_DGW.DataSource = bsChild;


                    tb_caen.DataBindings.Add("Text", bsChild, "caen", true);
                    tb_nume.DataBindings.Add("Text", bsChild, "nume", true);
                    tb_prenume.DataBindings.Add("Text", bsChild, "prenume", true);
                    tb_functie.DataBindings.Add("Text", bsChild, "functie", true);
                    tb_salar.DataBindings.Add("Text", bsChild, "salar", true);
                    tb_bonusuri.DataBindings.Add("Text", bsChild, "bonusuri", true);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void btn_refresh_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    parentAdapter.SelectCommand.Connection = connection;
                    childAdapter.SelectCommand.Connection = connection;
                    dataSet.Tables["Angajati"].Clear();
                    dataSet.Tables["Firma_transport"].Clear();
                    parentAdapter.Fill(dataSet, "Firma_transport");
                    childAdapter.Fill(dataSet, "Angajati");
                    MessageBox.Show("Refresh-ul a fost efectuat");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void btn_Update_Click(object sender, EventArgs e)
        {
                if (Angajati_DGW.SelectedRows.Count == 1)
                {
                if (ValidateUpdate())
                {
                    DataGridViewRow selectedRow = Angajati_DGW.SelectedRows[0];

                    string cnp = selectedRow.Cells["cnp"].Value.ToString();

                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {

                        string updateQuery = "UPDATE Angajati SET caen = @caen, nume = @nume, prenume = @prenume, functie = @functie, salar = @salar, bonusuri = @bonusuri WHERE cnp = @cnp";
                        SqlCommand updateCommand = new SqlCommand(updateQuery, connection);

                        updateCommand.Parameters.AddWithValue("@caen", tb_caen.Text);
                        updateCommand.Parameters.AddWithValue("@nume", tb_nume.Text);
                        updateCommand.Parameters.AddWithValue("@prenume", tb_prenume.Text);
                        updateCommand.Parameters.AddWithValue("@functie", tb_functie.Text);
                        updateCommand.Parameters.AddWithValue("@salar", tb_salar.Text);
                        updateCommand.Parameters.AddWithValue("@bonusuri", tb_bonusuri.Text);
                        updateCommand.Parameters.AddWithValue("@cnp", cnp);


                        connection.Open();
                        updateCommand.ExecuteNonQuery();
                        connection.Close();
                        MessageBox.Show("Update efectuat cu succes");
                    }   
                }else
                {
                MessageBox.Show("Selectați un rând din tabelul fiu.");
            }
            }          
        }
        private void btn_Delete_Click(object sender, EventArgs e)
        {
            if (Angajati_DGW.SelectedRows.Count == 1)
            {

                DataGridViewRow selectedRow = Angajati_DGW.SelectedRows[0];
                string cnp = selectedRow.Cells["cnp"].Value.ToString();

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    string deleteQuery = "DELETE FROM Angajati WHERE cnp = @cnp";
                    SqlCommand deleteCommand = new SqlCommand(deleteQuery, connection);

                    deleteCommand.Parameters.AddWithValue("@cnp", cnp);

                    connection.Open();
                    deleteCommand.ExecuteNonQuery();
                    connection.Close();
                    MessageBox.Show("Stergere efectuata cu succes");
                }
            }
            else
            {
                MessageBox.Show("Selectați un rând din tabelul fiu.");
            }
        }


        private bool ValidateAdaugare()
        {
            if (string.IsNullOrWhiteSpace(tb_cnpFT.Text) )
            {
                MessageBox.Show("Introduceți CNP-ul angajatului.");
                if(tb_cnpFT.Text.Length != 13)
                {
                    MessageBox.Show("CNP-ul trebuie sa contina 13 caractere");

                }
                return false;
            }

            if (string.IsNullOrWhiteSpace(tb_numeFT.Text))
            {
                MessageBox.Show("Introduceți numele angajatului.");
                return false;
            }

            if (string.IsNullOrWhiteSpace(tb_prenumeFT.Text))
            {
                MessageBox.Show("Introduceți prenumele angajatului.");
                return false;
            }

            if (string.IsNullOrWhiteSpace(tb_functieFT.Text))
            {
                MessageBox.Show("Introduceți funcția angajatului.");
                return false;
            }

            if (!decimal.TryParse(tb_salarFT.Text, out _))
            {
                MessageBox.Show("Introduceți un salariu valid (număr zecimal).");
                return false;
            }

            if (!decimal.TryParse(tb_bonusuriFT.Text, out _))
            {
                MessageBox.Show("Introduceți valoarea bonusurilor într-un format valid (număr zecimal).");
                return false;
            }

            return true;
        }

        private bool ValidateUpdate()
        {

            if (string.IsNullOrWhiteSpace(tb_nume.Text))
            {
                MessageBox.Show("Introduceți numele angajatului.");
                return false;
            }

            if (string.IsNullOrWhiteSpace(tb_prenume.Text))
            {
                MessageBox.Show("Introduceți prenumele angajatului.");
                return false;
            }

            if (string.IsNullOrWhiteSpace(tb_functie.Text))
            {
                MessageBox.Show("Introduceți funcția angajatului.");
                return false;
            }

            if (!decimal.TryParse(tb_salar.Text, out _))
            {
                MessageBox.Show("Introduceți un salariu valid (număr zecimal).");
                return false;
            }

            if (!decimal.TryParse(tb_bonusuri.Text, out _))
            {
                MessageBox.Show("Introduceți valoarea bonusurilor într-un format valid (număr zecimal).");
                return false;
            }

            return true;
        }

        private void btn_Add_Click(object sender, EventArgs e)
        {

                if (FIrma_DGW.SelectedRows.Count == 1)
                {
                if (ValidateAdaugare())
                {
                    DataGridViewRow selectedRow = FIrma_DGW.SelectedRows[0];
                    string caen = selectedRow.Cells["caen"].Value.ToString();

                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        string insertQuery = "INSERT INTO Angajati(cnp, caen, nume, prenume, functie, salar, bonusuri) VALUES (@cnp, @caen, @nume, @prenume, @functie, @salar, @bonusuri)";
                        SqlCommand insertCommand = new SqlCommand(insertQuery, connection);

                        insertCommand.Parameters.AddWithValue("@cnp", tb_cnpFT.Text);
                        insertCommand.Parameters.AddWithValue("@caen", caen);
                        insertCommand.Parameters.AddWithValue("@nume", tb_numeFT.Text);
                        insertCommand.Parameters.AddWithValue("@prenume", tb_prenumeFT.Text);
                        insertCommand.Parameters.AddWithValue("@functie", tb_functieFT.Text);
                        insertCommand.Parameters.AddWithValue("@salar", tb_salarFT.Text);
                        insertCommand.Parameters.AddWithValue("@bonusuri", tb_bonusuriFT.Text);

                        connection.Open();
                        insertCommand.ExecuteNonQuery();
                        connection.Close();
                        MessageBox.Show("Adaugare cu succes");
                    }
                }
                else
                {
                    MessageBox.Show("Selectați un rând din tabelul părinte.");
                }
            }      
 
        }
    }
}
