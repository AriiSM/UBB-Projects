using Microsoft.Data.SqlClient;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Configuration;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace SGBD_Lab2
{
    public partial class Form1 : Form
    {
        public static string parentTable = ConfigurationManager.AppSettings.Get("ParentTableName");
        public static string childTable = ConfigurationManager.AppSettings.Get("ChildTableName");
        public static string parentPrimaryKey = ConfigurationManager.AppSettings.Get("ParentPrimaryKey");
        public static string childForeignKey = ConfigurationManager.AppSettings.Get("ChilForeignKey");
        public static string childPrimaryKey = ConfigurationManager.AppSettings.Get("ChilPrimaryKey");
        public static string childColumnNames = ConfigurationManager.AppSettings.Get("ChildColumnNames");
        public static string childInsertColumnNames = ConfigurationManager.AppSettings.Get("InsertChildColumnNames");

        string[] childCName = childColumnNames.Split(',');

        static string conn = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;


        DataSet dataSet = new DataSet();
        SqlDataAdapter parentAdapter;
        SqlDataAdapter childAdapter;
        BindingSource bsParent;
        BindingSource bsChild;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(conn))
                {
                    connection.Open();
                    string ParentSelect = "SELECT * FROM " + parentTable;
                    string ChildSelect = "SELECT * FROM " + childTable;

                    parentAdapter = new SqlDataAdapter(ParentSelect, connection);
                    childAdapter = new SqlDataAdapter(ChildSelect, connection);

                    parentAdapter.Fill(dataSet, parentTable);
                    childAdapter.Fill(dataSet, childTable);

                    DataColumn pkColumn = dataSet.Tables[parentTable].Columns[parentPrimaryKey];
                    DataColumn fkColumn = dataSet.Tables[childTable].Columns[childForeignKey];

                    DataRelation relation = new DataRelation("Relatie", pkColumn, fkColumn);

                    dataSet.Relations.Add(relation);

                    bsParent = new BindingSource();
                    bsChild = new BindingSource();

                    bsParent.DataSource = dataSet.Tables[parentTable];
                    parentDateGrid.DataSource = bsParent;


                    bsChild.DataMember = "Relatie";
                    bsChild.DataSource = bsParent;
                    ChildDataGrid.DataSource = bsChild;

                    parentDateGrid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                    ChildDataGrid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

        }
        private void btn_generare_campuri_Click(object sender, EventArgs e)
        {
            int n = childCName.Length;
            for (int i = 0; i < n; i++)
            {
                TextBox textBox = new TextBox();
                textBox.Location = new System.Drawing.Point(800, 50 + i * 30); // Ajustează locația pentru fiecare TextBox
                textBox.Size = new System.Drawing.Size(200, 20); // Setează dimensiunea TextBox-ului
                this.Controls.Add(textBox); // Adaugă TextBox-ul la Form
                textBox.Name = childCName[i];
                textBox.PlaceholderText = childCName[i];
            }
        }

        private void ChildDataGrid_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0 && e.RowIndex < ChildDataGrid.Rows.Count)
            {
                DataGridViewRow selectedRow = ChildDataGrid.Rows[e.RowIndex];

                if (selectedRow.Cells.Count == childCName.Length)
                {
                    for (int i = 0; i < childCName.Length; i++)
                    {
                        TextBox textBox = (TextBox)this.Controls[childCName[i]];
                        textBox.Text = selectedRow.Cells[i].Value.ToString();
                    }
                }
                else
                {
                    MessageBox.Show("Numărul de celule din rând nu corespunde numărului de TextBox-uri create.");
                }
            }
        }

        private void btn_regresh_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(conn))
                {
                    parentAdapter.SelectCommand.Connection = connection;
                    childAdapter.SelectCommand.Connection = connection;

                    dataSet.Tables[childTable].Clear();
                    dataSet.Tables[parentTable].Clear();
                    parentAdapter.Fill(dataSet, parentTable);
                    childAdapter.Fill(dataSet, childTable);
                    MessageBox.Show("Refresh-ul a fost efectuat");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btn_inserare_Click(object sender, EventArgs e)
        {
            if (parentDateGrid.SelectedRows.Count == 1)
            {
                DataGridViewRow selectedRow = parentDateGrid.SelectedRows[0];
                string pk = selectedRow.Cells[parentPrimaryKey].Value.ToString();

                using (SqlConnection connection = new SqlConnection(conn))
                {
                    string insertQuery = $"INSERT INTO {childTable} ({childColumnNames}) VALUES ({childInsertColumnNames})";
                    SqlCommand insertCommand = new SqlCommand(insertQuery, connection);

                    insertCommand.Parameters.AddWithValue("@"+ childForeignKey, pk);

                    foreach (string columnName in childCName)
                    { 
                        if (columnName != parentPrimaryKey)
                        {
                            string columnValue = ((TextBox)this.Controls[columnName]).Text;
                            insertCommand.Parameters.AddWithValue($"@{columnName}", columnValue);
                        }
                    }

                    try
                    {
                        connection.Open();
                        insertCommand.ExecuteNonQuery();
                        MessageBox.Show("Adaugare cu succes");
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show("Eroare la inserare: " + ex.Message);
                    }
                    finally
                    {
                        connection.Close();
                    }
                }
            }
            else
            {
                MessageBox.Show("Selectați un rând din tabelul părinte.");
            }
        }

        private void btn_update_Click(object sender, EventArgs e)
        {
            if (ChildDataGrid.SelectedRows.Count == 1)
            {
                DataGridViewRow selectedRow = ChildDataGrid.SelectedRows[0];
                string childPrimaryKeyValue = selectedRow.Cells[childPrimaryKey].Value.ToString();

                if (MessageBox.Show("Sunteți sigur că doriți să actualizați acest câmp din tabelul copil?", "Confirmare actualizare", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    using (SqlConnection connection = new SqlConnection(conn))
                    {
                        string updateQuery = $"UPDATE {childTable} SET ";
                        for (int i = 0; i < childCName.Length; i++)
                        {
                            // Adaugă numele coloanei și valoarea parametrilor la comanda SQL
                            string columnName = childCName[i];
                            updateQuery += $"{columnName} = @{columnName}";

                            // Adaugă virgulă între coloane, exceptând ultima
                            if (i < childCName.Length - 1)
                            {
                                updateQuery += ", ";
                            }
                        }
                        updateQuery += $" WHERE {childPrimaryKey} = @ChildPrimaryKeyValue";

                        SqlCommand updateCommand = new SqlCommand(updateQuery, connection);
                        updateCommand.Parameters.AddWithValue("@ChildPrimaryKeyValue", childPrimaryKeyValue);

                        foreach (string columnName in childCName)
                        {
                            // Extrageți valoarea din TextBox-ul corespunzător numelui coloanei
                            string columnValue = ((TextBox)this.Controls[columnName]).Text;

                            // Adaugă parametrii pentru fiecare coloană la comanda SQL
                            updateCommand.Parameters.AddWithValue($"@{columnName}", columnValue);
                        }

                        try
                        {
                            connection.Open();
                            int rowsAffected = updateCommand.ExecuteNonQuery();
                            if (rowsAffected > 0)
                            {
                                MessageBox.Show("Actualizare cu succes");
                            }
                            else
                            {
                                MessageBox.Show("Nicio înregistrare nu a fost actualizată.");
                            }
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Eroare la actualizare: " + ex.Message);
                        }
                        finally
                        {
                            connection.Close();
                        }
                    }
                }
            }
            else
            {
                MessageBox.Show("Selectați un rând din tabelul copil pentru a-l actualiza.");
            }
        }

        private void btn_delete_Click(object sender, EventArgs e)
        {
            if (ChildDataGrid.SelectedRows.Count == 1)
            {
                DataGridViewRow selectedRow = ChildDataGrid.SelectedRows[0];
                string childPrimaryKeyValue = selectedRow.Cells[childPrimaryKey].Value.ToString();

                if (MessageBox.Show("Sunteți sigur că doriți să ștergeți acest câmp din tabelul copil?", "Confirmare ștergere", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    using (SqlConnection connection = new SqlConnection(conn))
                    {
                        string deleteQuery = $"DELETE FROM {childTable} WHERE {childPrimaryKey} = @ChildPrimaryKeyValue";
                        SqlCommand deleteCommand = new SqlCommand(deleteQuery, connection);
                        deleteCommand.Parameters.AddWithValue("@ChildPrimaryKeyValue", childPrimaryKeyValue);

                        try
                        {
                            connection.Open();
                            int rowsAffected = deleteCommand.ExecuteNonQuery();
                            if (rowsAffected > 0)
                            {
                                MessageBox.Show("Ștergere cu succes");
                            }
                            else
                            {
                                MessageBox.Show("Nicio înregistrare nu a fost ștearsă.");
                            }
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Eroare la ștergere: " + ex.Message);
                        }
                        finally
                        {
                            connection.Close();
                        }
                    }
                }
            }
            else
            {
                MessageBox.Show("Selectați un rând din tabelul copil pentru a-l șterge.");
            }
        }

    }
}
