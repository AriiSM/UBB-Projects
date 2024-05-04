namespace SGBD_Lab2
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            parentDateGrid = new DataGridView();
            ChildDataGrid = new DataGridView();
            btn_generare_campuri = new Button();
            btn_inserare = new Button();
            btn_update = new Button();
            btn_delete = new Button();
            btn_regresh = new Button();
            ((System.ComponentModel.ISupportInitialize)parentDateGrid).BeginInit();
            ((System.ComponentModel.ISupportInitialize)ChildDataGrid).BeginInit();
            SuspendLayout();
            // 
            // parentDateGrid
            // 
            parentDateGrid.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            parentDateGrid.Location = new Point(24, 41);
            parentDateGrid.Name = "parentDateGrid";
            parentDateGrid.RowHeadersWidth = 51;
            parentDateGrid.RowTemplate.Height = 29;
            parentDateGrid.Size = new Size(698, 260);
            parentDateGrid.TabIndex = 0;
            // 
            // ChildDataGrid
            // 
            ChildDataGrid.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            ChildDataGrid.Location = new Point(24, 460);
            ChildDataGrid.Name = "ChildDataGrid";
            ChildDataGrid.RowHeadersWidth = 51;
            ChildDataGrid.RowTemplate.Height = 29;
            ChildDataGrid.Size = new Size(1004, 207);
            ChildDataGrid.TabIndex = 1;
            ChildDataGrid.CellContentClick += ChildDataGrid_CellContentClick;
            // 
            // btn_generare_campuri
            // 
            btn_generare_campuri.Location = new Point(24, 307);
            btn_generare_campuri.Name = "btn_generare_campuri";
            btn_generare_campuri.Size = new Size(180, 29);
            btn_generare_campuri.TabIndex = 2;
            btn_generare_campuri.Text = "Generare Campuri";
            btn_generare_campuri.UseVisualStyleBackColor = true;
            btn_generare_campuri.Click += btn_generare_campuri_Click;
            // 
            // btn_inserare
            // 
            btn_inserare.Location = new Point(93, 369);
            btn_inserare.Name = "btn_inserare";
            btn_inserare.Size = new Size(94, 29);
            btn_inserare.TabIndex = 3;
            btn_inserare.Text = "Inserare";
            btn_inserare.UseVisualStyleBackColor = true;
            btn_inserare.Click += btn_inserare_Click;
            // 
            // btn_update
            // 
            btn_update.Location = new Point(226, 369);
            btn_update.Name = "btn_update";
            btn_update.Size = new Size(94, 29);
            btn_update.TabIndex = 4;
            btn_update.Text = "Update";
            btn_update.UseVisualStyleBackColor = true;
            btn_update.Click += btn_update_Click;
            // 
            // btn_delete
            // 
            btn_delete.Location = new Point(376, 369);
            btn_delete.Name = "btn_delete";
            btn_delete.Size = new Size(94, 29);
            btn_delete.TabIndex = 5;
            btn_delete.Text = "Delete";
            btn_delete.UseVisualStyleBackColor = true;
            btn_delete.Click += btn_delete_Click;
            // 
            // btn_regresh
            // 
            btn_regresh.Location = new Point(24, 425);
            btn_regresh.Name = "btn_regresh";
            btn_regresh.Size = new Size(94, 29);
            btn_regresh.TabIndex = 6;
            btn_regresh.Text = "Refresh";
            btn_regresh.UseVisualStyleBackColor = true;
            btn_regresh.Click += btn_regresh_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1084, 698);
            Controls.Add(btn_regresh);
            Controls.Add(btn_delete);
            Controls.Add(btn_update);
            Controls.Add(btn_inserare);
            Controls.Add(btn_generare_campuri);
            Controls.Add(ChildDataGrid);
            Controls.Add(parentDateGrid);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ((System.ComponentModel.ISupportInitialize)parentDateGrid).EndInit();
            ((System.ComponentModel.ISupportInitialize)ChildDataGrid).EndInit();
            ResumeLayout(false);
        }

        #endregion

        private DataGridView parentDateGrid;
        private DataGridView ChildDataGrid;
        private Button btn_generare_campuri;
        private Button btn_inserare;
        private Button btn_update;
        private Button btn_delete;
        private Button btn_regresh;
    }
}