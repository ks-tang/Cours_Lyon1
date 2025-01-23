#include "qte.h"
#include "implicits.h"
#include "ui_interface.h"
#include "time.h"

Mesh mesh;
int rX;
int rY;
int rZ;
Matrix m;
QImage img;
Vector v;
clock_t t1, t2;

MainWindow::MainWindow() : QMainWindow(), uiw(new Ui::Assets)
{
	// Chargement de l'interface
    uiw->setupUi(this);

	// Chargement du GLWidget
	meshWidget = new MeshWidget;
	QGridLayout* GLlayout = new QGridLayout;
	GLlayout->addWidget(meshWidget, 0, 0);
	GLlayout->setContentsMargins(0, 0, 0, 0);
    uiw->widget_GL->setLayout(GLlayout);

	// Creation des connect
	CreateActions();

	meshWidget->SetCamera(Camera(Vector(10, 0, 0), Vector(0.0, 0.0, 0.0)));
}

MainWindow::~MainWindow()
{
	delete meshWidget;
}

void MainWindow::CreateActions()
{
	// Buttons
    connect(uiw->boxMesh, SIGNAL(clicked()), this, SLOT(BoxMeshExample()));
    connect(uiw->sphereImplicit, SIGNAL(clicked()), this, SLOT(SphereImplicitExample()));
    connect(uiw->resetcameraButton, SIGNAL(clicked()), this, SLOT(ResetCamera()));
    connect(uiw->wireframe, SIGNAL(clicked()), this, SLOT(UpdateMaterial()));
    connect(uiw->radioShadingButton_1, SIGNAL(clicked()), this, SLOT(UpdateMaterial()));
    connect(uiw->radioShadingButton_2, SIGNAL(clicked()), this, SLOT(UpdateMaterial()));
	connect(uiw->circleMesh, SIGNAL(clicked()), this, SLOT(circleExample()));
	connect(uiw->cylinderMesh, SIGNAL(clicked()), this, SLOT(cylinderExample()));
	connect(uiw->sphereMesh, SIGNAL(clicked()), this, SLOT(sphereExample()));
	connect(uiw->toreMesh, SIGNAL(clicked()), this, SLOT(toreExample()));
	connect(uiw->capsuleMesh, SIGNAL(clicked()), this, SLOT(capsuleExample()));
	connect(uiw->sliderX, SIGNAL(valueChanged(int)), this, SLOT(sliderX(int)));
	connect(uiw->sliderY, SIGNAL(valueChanged(int)), this, SLOT(sliderY(int)));
	connect(uiw->sliderZ, SIGNAL(valueChanged(int)), this, SLOT(sliderZ(int)));

	connect(uiw->createField, SIGNAL(clicked()), this, SLOT(createField()));
	connect(uiw->loadImage, SIGNAL(clicked()), this, SLOT(loadImage()));

	connect(uiw->vectorX, SIGNAL(valueChanged(double)), this, SLOT(vectorX(double)));
	connect(uiw->vectorY, SIGNAL(valueChanged(double)), this, SLOT(vectorY(double)));
	connect(uiw->vectorZ, SIGNAL(valueChanged(double)), this, SLOT(vectorZ(double)));
	connect(uiw->pointWarp, SIGNAL(clicked()), this, SLOT(pointWarp()));

	// Widget edition
	connect(meshWidget, SIGNAL(_signalEditSceneLeft(const Ray&)), this, SLOT(editingSceneLeft(const Ray&)));
	connect(meshWidget, SIGNAL(_signalEditSceneRight(const Ray&)), this, SLOT(editingSceneRight(const Ray&)));
}

void MainWindow::editingSceneLeft(const Ray&)
{
}

void MainWindow::editingSceneRight(const Ray&)
{
}

void MainWindow::BoxMeshExample()
{
	mesh = Mesh(Box(1.0));

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	UpdateGeometry();
}

void MainWindow::SphereImplicitExample()
{
  AnalyticScalarField implicit;

  Mesh implicitMesh;
  implicit.Polygonize(31, implicitMesh, Box(2.0));

  std::vector<Color> cols;
  cols.resize(implicitMesh.Vertexes());
  for (size_t i = 0; i < cols.size(); i++)
    cols[i] = Color(0.8, 0.8, 0.8);

  meshColor = MeshColor(implicitMesh, cols, implicitMesh.VertexIndexes());
  UpdateGeometry();
}

void MainWindow::UpdateGeometry()
{
	meshWidget->ClearAll();
	meshWidget->AddMesh("BoxMesh", meshColor);

    uiw->lineEdit->setText(QString::number(meshColor.Vertexes()));
    uiw->lineEdit_2->setText(QString::number(meshColor.Triangles()));
	uiw->elapsedTime->setText(QString::number((float)(t2-t1)/(CLOCKS_PER_SEC/1000)));

	UpdateMaterial();
}

void MainWindow::UpdateMaterial()
{
    meshWidget->UseWireframeGlobal(uiw->wireframe->isChecked());

    if (uiw->radioShadingButton_1->isChecked())
		meshWidget->SetMaterialGlobal(MeshMaterial::Normal);
	else
		meshWidget->SetMaterialGlobal(MeshMaterial::Color);
}

void MainWindow::ResetCamera()
{
	meshWidget->SetCamera(Camera(Vector(-10.0), Vector(0.0)));
}

void MainWindow::circleExample() {
	t1 = clock();
	mesh = Mesh(Circle(Vector(0,0,0), 5, 100));

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::cylinderExample() {
	t1 = clock();
	mesh = Mesh(Cylinder(Vector(0,0,0),5,5, 20));
	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::sphereExample() {
	t1 = clock();
	mesh = Mesh(Sphere(Vector(0,0,0),1,100, 100));

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::toreExample() {
	t1 = clock();
	mesh = Mesh(Tore(Circle(Vector(0,0,0), 5, 100), 1 , 100 ));

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::capsuleExample() {
	t1 = clock();
	mesh = Mesh(Demisphere(Vector(0,0,0),1,50, 50));
	Mesh cylinderMesh = Mesh(Cylinder(Vector(0,0,0),1,-3, 100));
	Mesh demisphereMesh = Mesh(Demisphere(Vector(0,3,0),1,50, 50));
	m.rotateX(180);
	demisphereMesh.rotate(m);
	mesh.merge(cylinderMesh);
	mesh.merge(demisphereMesh);

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::sliderX(int value) {
	rX = value;
	meshRotationX();
}

void MainWindow::meshRotationX()
{
	m.rotateX(rX/20);
	mesh.rotate(m);
	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	UpdateGeometry();
}

void MainWindow::sliderY(int value) {
	rY = value;
	meshRotationY();
}

void MainWindow::meshRotationY()
{
	m.rotateY(rY/20);
	mesh.rotate(m);
	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	UpdateGeometry();
}

void MainWindow::sliderZ(int value) {
	rZ = value;
	meshRotationZ();
}

void MainWindow::meshRotationZ()
{
	m.rotateZ(rZ/20);
	mesh.rotate(m);
	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	UpdateGeometry();
}

void MainWindow::loadImage() {
    QString filename = QFileDialog::getOpenFileName(this, "Choose image");
    if(filename.isEmpty()){
		return;
	}
    img.load(filename);
}

void MainWindow::createField() {
	t1 = clock();
	mesh = Mesh(Heightfield(img));

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}

void MainWindow::vectorX(double value){
	v[0] = value;
}

void MainWindow::vectorY(double value){
	v[1] = value;
}

void MainWindow::vectorZ(double value){
	v[2] = value;
}

void MainWindow::pointWarp(){
	t1= clock();
	mesh.pointWarp(v);

	std::vector<Color> cols;
	cols.resize(mesh.Vertexes());
    for (size_t i = 0; i < cols.size(); i++)
		cols[i] = Color(double(i) / 6.0, fmod(double(i) * 39.478378, 1.0), 0.0);

	meshColor = MeshColor(mesh, cols, mesh.VertexIndexes());
	t2 = clock();
	UpdateGeometry();
}
